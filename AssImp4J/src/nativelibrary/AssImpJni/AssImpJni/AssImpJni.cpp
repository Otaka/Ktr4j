// AssImpJni.cpp : Defines the exported functions for the DLL application.

#include <jni.h>
#include <Windows.h>
#include <string>
#include <exception>
#include<map>
#include "assimp/cimport.h"
#include "assimp/scene.h"
#include "assimp/postprocess.h"




class ustring;



std::wstring stringToWstring(std::string&s);
std::string Java_To_Str(JNIEnv *env, jstring string);


aiScene* (*_aiImportFileDll)(const char* pFile,unsigned int pFlags);
aiScene* (*_aiImportFileFromMemory)(const char* pBuffer,unsigned int pLength,unsigned int pFlags,const char* pHint);
void (*_aiReleaseImport)(aiScene* pScene);
aiScene* (*_aiApplyPostProcessing)(aiScene* pScene, unsigned int pFlags);
aiBool (*_aiIsExtensionSupported)(const char* szExtension);
void (*_aiDecomposeMatrix)(aiMatrix4x4* mat, aiVector3D* scaling, aiQuaternion* rotation, aiVector3D* position);
aiReturn (*_aiGetMaterialString)(const aiMaterial* pMat,const char* pKey,unsigned int type,unsigned int index,aiString* pOut);
aiReturn (*_aiGetMaterialFloatArray)(const aiMaterial* pMat,const char* pKey,unsigned int type,unsigned int index,ai_real* pOut,unsigned int* pMax);
aiReturn (*_aiGetMaterialIntegerArray)(const aiMaterial* pMat,const char* pKey,unsigned int  type,unsigned int  index,int* pOut,unsigned int* pMax);
aiReturn(*_aiGetMaterialTexture)(const aiMaterial* mat, aiTextureType type, unsigned int  index, aiString* path, /*all other can be NULL*/aiTextureMapping* mapping,unsigned int* uvindex,ai_real* blend,aiTextureOp* op,aiTextureMapMode* mapmode,unsigned int* flags);


std::map<ustring, jobject> methodCache;
jmethodID findMethodMethodId;

void clearMethodCache() {
	methodCache.clear();
}

std::wstring Java_To_WStr(JNIEnv *env, jstring string) {
	std::wstring value;
	jboolean copy = false;
	wchar_t* raw= (wchar_t*)env->GetStringChars(string, &copy);
	value.assign(raw);
	env->ReleaseStringChars(string, (const jchar*)raw);
	return value;
}

std::string Java_To_Str(JNIEnv *env, jstring string) {
	std::string value;
	const jchar *raw = env->GetStringChars(string, 0);
	jsize len = env->GetStringLength(string);
	const jchar *temp = raw;
	value.assign(raw, raw + len);
	env->ReleaseStringChars(string, raw);
	return value;
}


std::wstring stringToWstring(std::string&s) {
	std::wstring ws(s.size(), L' '); // Overestimate number of code points.
	ws.resize(mbstowcs(&ws[0], s.c_str(), s.size()));
	return ws;
}


class ustring :public std::string {
public:
	ustring(const char*value) {
		assign(value);
	}

	ustring(std::string&str) {
		assign(str.c_str());
	}

	ustring(JNIEnv*env,jstring str) {
		assign(Java_To_Str(env,str).c_str());
	}

	ustring(int value) {
		char buffer[11];
		_itoa_s(value, buffer,10);
		assign(buffer);
	}

	ustring(float value) {
		char buffer[20];
		sprintf_s(buffer,20, "%f", value);
		assign(buffer);
	}

	ustring& operator+(ustring&rhs) {
		append(rhs.c_str());
		return *this;
	}

	std::string to_std_string() {
		std::string str(this->c_str());
		return str;
	}
};

std::exception createException(ustring str) {
	return std::exception(str.c_str());
}

jint throwNoClassDefError(JNIEnv *env, const char *message) {
	jclass exClass;
	char *className = "java/lang/NoClassDefFoundError";

	exClass = env->FindClass(className);
	if (exClass == NULL) {
		throw ustring("Class [")+ustring(className)+ustring("] is not found");
	}

	return env->ThrowNew(exClass, message);
}

jint throwRuntimeException(JNIEnv *env, const char *message)
{
	jclass exClass;
	char *className = "java/lang/RuntimeException";

	exClass = env->FindClass(className);
	if (exClass == NULL) {
		return throwNoClassDefError(env, className);
	}

	return env->ThrowNew(exClass, message);
}

void importFunction(JNIEnv*env, HMODULE moduleHandle, const char*functionName, void**functionPointer) {
	void* pointer = (void*)GetProcAddress(moduleHandle, functionName);
	if (pointer == NULL) {
		throw createException(ustring("Cannot find function [")+ustring(functionName)+ustring("] in AssImp library"));
	}
	else {
		*functionPointer = pointer;
	}
}

void putInMethodCache(ustring&key, jobject value) {
	methodCache[key] = value;
}

jobject getFromMethodCache(ustring&key) {
	return methodCache[key];
}

bool containsInMethodCache(ustring&key) {
	return !(methodCache.find(key) == methodCache.end());
}

jclass findClass(JNIEnv*env, ustring&classPath) {
	if (containsInMethodCache(ustring(classPath))) {
		return (jclass)getFromMethodCache(classPath);
	}
	else {
		jclass cls = env->FindClass(classPath.c_str());
		if (cls == NULL) {
			throw createException( ustring("Cannot find class [")+classPath+ustring("]"));
		}

		cls = (jclass)env->NewGlobalRef(cls);
		putInMethodCache(classPath,cls);
		return cls;
	}
}

/*
	classMethodNameWithSignature should be like this:[class|mymethod|()V] - class+"|"+methodName+"|"+signature
*/
jmethodID findMethod(JNIEnv*env,boolean staticMethod,ustring&classMethodNameWithSignature) {
	if (containsInMethodCache(classMethodNameWithSignature)) {
		return (jmethodID)getFromMethodCache(classMethodNameWithSignature);
	}
	else {
		ustring tCopyOfClassMethodNameWithSig = classMethodNameWithSignature;
		char*tMethodNameWithSig = (char*)tCopyOfClassMethodNameWithSig.c_str();
		char*className = tMethodNameWithSig;
		char*methodName=NULL;
		char*signature=NULL;
		size_t length = classMethodNameWithSignature.length();
		
		int state = 0;
		for (int i = 0; i < length; i++) {
			if (className[i] == '|') {
				if (state == 0) {
					className[i] = 0;
					methodName = &(className[i + 1]);
					state = 1;
				}
				else {
					className[i] = 0;
					signature = &(className[i + 1]);
					state = 2;
				}
			}
		}

		if (methodName == NULL|| signature==NULL) {
			throw createException(ustring("Cannot find [|] symbol in methodNameWithSignature [")+ classMethodNameWithSignature +ustring("]"));
		}

		jclass cls = findClass(env,ustring(className));
		jmethodID constructor;
		if (staticMethod) {
			constructor = env->GetStaticMethodID(cls, methodName, signature);
		}
		else {
			constructor = env->GetMethodID(cls, methodName, signature);
		}
		if (constructor == NULL) {
			throw createException(ustring("Cannot find method [") + classMethodNameWithSignature + ustring("]"));
		}
		putInMethodCache(classMethodNameWithSignature, (jobject)constructor);
		return constructor;
	}
}

jmethodID findMethodIgnoreSignature(JNIEnv*env,bool staticMethod, ustring&classNameMethodName) {
	if (containsInMethodCache(classNameMethodName)) {
		return (jmethodID)getFromMethodCache(classNameMethodName);
	}
	else {
		ustring tClassNameMethodName = classNameMethodName;
		char*tMethodNameWithSig = (char*)tClassNameMethodName.c_str();
		char*className = tMethodNameWithSig;
		char*methodName = NULL;
		
		size_t length = classNameMethodName.length();

		for (int i = 0; i < length; i++) {
			if (className[i] == '|') {
				className[i] = 0;
				methodName = &(className[i + 1]);
			}
		}

		if (methodName == NULL) {
			throw createException(ustring("Cannot find [|] symbol in classNameMethodName [") + classNameMethodName + ustring("]"));
		}
		jclass assImpClass = findClass(env, ustring("com/assimp4j/AssImp"));
		jclass classToFind= findClass(env, ustring(className));
		jstring methodNameJstring = env->NewStringUTF(methodName);
		jstring resultJString=(jstring)env->CallStaticObjectMethod(assImpClass,findMethodMethodId, classToFind, methodNameJstring);
		if (resultJString == NULL) {
			throw createException(ustring("Class [")+ustring(className)+ustring("] does not have method [")+ustring(methodName)+ustring("]"));
		}

		ustring foundMethodSignature = ustring(env, resultJString);
		ustring combinedClassNameMethodSig = ustring(className) + ustring("|") + ustring(methodName) + ustring("|") + foundMethodSignature;
		jmethodID foundMethodId= findMethod(env,staticMethod, combinedClassNameMethodSig);
		if (foundMethodId == NULL) {
			throw createException(ustring("Cannot find method [") + combinedClassNameMethodSig + ustring("]"));
		}
		putInMethodCache(classNameMethodName, (jobject)foundMethodId);
		return foundMethodId;
	}
}

jobject instantiateObject(JNIEnv *env, ustring&classPath) {
	jclass cls = findClass(env, classPath);
	ustring methodWithSignature = ustring(classPath) + ustring("|<init>|()V");
	jmethodID constructor =findMethod(env,false, methodWithSignature);
	jobject object = env->NewObject(cls, constructor);
	return object;
}

void callSetterMethodWithStringArgument(JNIEnv*env,jobject owner, ustring classMethod,const char*value) {
	jmethodID method = findMethodIgnoreSignature(env, false, classMethod);
	jstring javaString=env->NewStringUTF(value);
	env->CallVoidMethod(owner,method,javaString);
}

void callSetterMethodWithObjectArgument(JNIEnv*env, jobject owner, ustring classMethod, jobject value) {
	jmethodID method = findMethodIgnoreSignature(env, false, classMethod);
	env->CallVoidMethod(owner, method, value);
}

void callSetterMethodWithFloatArgument(JNIEnv*env, jobject owner, ustring classMethod, float value) {
	jmethodID method = findMethodIgnoreSignature(env, false, classMethod);
	env->CallVoidMethod(owner, method, value);
}

void callSetterMethodWithDoubleArgument(JNIEnv*env, jobject owner, ustring classMethod, double value) {
	jmethodID method = findMethodIgnoreSignature(env, false, classMethod);
	env->CallVoidMethod(owner, method, value);
}

void callSetterMethodWithIntArgument(JNIEnv*env, jobject owner, ustring classMethod, int value) {
	jmethodID method = findMethodIgnoreSignature(env, false, classMethod);
	env->CallVoidMethod(owner, method, value);
}

void callSetterMethodWithBoolArgument(JNIEnv*env, jobject owner, ustring classMethod, bool value) {
	jmethodID method = findMethodIgnoreSignature(env, false, classMethod);
	env->CallVoidMethod(owner, method, value);
}

jfloatArray convertMatrix4x4ToJavaArray(JNIEnv*env, aiMatrix4x4*matrix) {
	jfloatArray tArray = env->NewFloatArray(16);
	jfloat*tArrayElements=env->GetFloatArrayElements(tArray,NULL);
	tArrayElements[0] = matrix->a1;
	tArrayElements[1] = matrix->a2;
	tArrayElements[2] = matrix->a3;
	tArrayElements[3] = matrix->a4;
	tArrayElements[4] = matrix->b1;
	tArrayElements[5] = matrix->b2;
	tArrayElements[6] = matrix->b3;
	tArrayElements[7] = matrix->b4;
	tArrayElements[8] = matrix->c1;
	tArrayElements[9] = matrix->c2;
	tArrayElements[10] = matrix->c3;
	tArrayElements[11] = matrix->c4;
	tArrayElements[12] = matrix->d1;
	tArrayElements[13] = matrix->d2;
	tArrayElements[14] = matrix->d3;
	tArrayElements[15] = matrix->d4;
	env->ReleaseFloatArrayElements(tArray, tArrayElements, 0);
	return tArray;
}

jfloatArray convertVector3fToJavaArray(JNIEnv*env, aiVector3D*vector) {
	jfloatArray tArray = env->NewFloatArray(3);
	jfloat*tArrayElements = env->GetFloatArrayElements(tArray, NULL);
	tArrayElements[0] = vector->x;
	tArrayElements[1] = vector->y;
	tArrayElements[2] = vector->z;
	env->ReleaseFloatArrayElements(tArray, tArrayElements, 0);
	return tArray;
}

jfloatArray convertQuaternionToJavaArray(JNIEnv*env, aiQuaternion*quaternion) {
	jfloatArray tArray = env->NewFloatArray(4);
	jfloat*tArrayElements = env->GetFloatArrayElements(tArray, NULL);
	tArrayElements[0] = quaternion->x;
	tArrayElements[1] = quaternion->y;
	tArrayElements[2] = quaternion->z;
	tArrayElements[3] = quaternion->w;
	env->ReleaseFloatArrayElements(tArray, tArrayElements, 0);
	return tArray;
}

void setAiBoneVerticesWeight(JNIEnv*env, aiBone*bone, jobject boneObject) {
	jintArray verticesIndexesArray = env->NewIntArray(bone->mNumWeights);
	jfloatArray verticesWeightsArray = env->NewFloatArray(bone->mNumWeights);

	jint* indexes = env->GetIntArrayElements(verticesIndexesArray,NULL);
	jfloat* weights = env->GetFloatArrayElements(verticesWeightsArray, NULL);
	for (unsigned int i = 0; i < bone->mNumWeights; i++) {
		aiVertexWeight*vw= &bone->mWeights[i];
		indexes[i] = vw->mVertexId;
		weights[i] = vw->mWeight;
	}

	env->ReleaseIntArrayElements(verticesIndexesArray, indexes, 0);
	env->ReleaseFloatArrayElements(verticesWeightsArray, weights, 0);
	callSetterMethodWithObjectArgument(env, boneObject, "com/assimp4j/data/AiBone|setVertexWeights_VertexIndexArray",verticesIndexesArray);
	callSetterMethodWithObjectArgument(env, boneObject, "com/assimp4j/data/AiBone|setVertexWeights_VertexWeightArray", verticesWeightsArray);
}

void processMesh(JNIEnv*env,aiMesh*mesh, jobject aiSceneJavaObject) {
	jobject meshObject = instantiateObject(env, ustring("com/assimp4j/data/AiMesh"));
	callSetterMethodWithObjectArgument(env, aiSceneJavaObject, "com/assimp4j/data/AiScene|addMesh", meshObject);

	callSetterMethodWithIntArgument(env, meshObject, "com/assimp4j/data/AiMesh|setMaterialIndex", mesh->mMaterialIndex);
	callSetterMethodWithStringArgument(env, meshObject, "com/assimp4j/data/AiMesh|setMeshName", mesh->mName.C_Str());
	//POSITIONS
	unsigned int verticesCount = mesh->mNumVertices;
	jfloatArray positionArray = env->NewFloatArray(verticesCount * 3);
	float*positionArrayValues = env->GetFloatArrayElements(positionArray, NULL);
	int index = 0;
	for (unsigned int i = 0; i < verticesCount; i++, index += 3) {
		positionArrayValues[index + 0] = mesh->mVertices[i].x;
		positionArrayValues[index + 1] = mesh->mVertices[i].y;
		positionArrayValues[index + 2] = mesh->mVertices[i].z;
	}
	env->ReleaseFloatArrayElements(positionArray, positionArrayValues, 0);
	callSetterMethodWithObjectArgument(env, meshObject, "com/assimp4j/data/AiMesh|setPositions", positionArray);

	if (mesh->HasNormals()) {
		jfloatArray normalsArray = env->NewFloatArray(verticesCount * 3);
		float*normalsArrayValues = env->GetFloatArrayElements(normalsArray, NULL);
		index = 0;
		for (unsigned int i = 0; i < verticesCount; i++, index += 3) {
			normalsArrayValues[index + 0] = mesh->mNormals[i].x;
			normalsArrayValues[index + 1] = mesh->mNormals[i].y;
			normalsArrayValues[index + 2] = mesh->mNormals[i].z;
		}
		env->ReleaseFloatArrayElements(normalsArray, normalsArrayValues, 0);
		callSetterMethodWithObjectArgument(env, meshObject, "com/assimp4j/data/AiMesh|setNormals", normalsArray);
	}

	if (mesh->HasTextureCoords(0)) {
		int numUvChannels = mesh->GetNumUVChannels();
		int numUvComponents = mesh->mNumUVComponents[0];
		jfloatArray uvArray = env->NewFloatArray(verticesCount * 2);
		float*uvArrayValues = env->GetFloatArrayElements(uvArray, NULL);
		index = 0;
		for (unsigned int i = 0; i < verticesCount; i++, index += 2) {
			float x = mesh->mTextureCoords[0][i].x;
			float y = mesh->mTextureCoords[0][i].y;

			uvArrayValues[index + 0] = x;
			uvArrayValues[index + 1] = y;
		}
		env->ReleaseFloatArrayElements(uvArray, uvArrayValues, 0);
		callSetterMethodWithObjectArgument(env, meshObject, "com/assimp4j/data/AiMesh|setUv", uvArray);
	}

	if (mesh->HasVertexColors(0)) {
		jfloatArray vertexColorArray = env->NewFloatArray(verticesCount * 3);
		float*vertexColorValues = env->GetFloatArrayElements(vertexColorArray, NULL);
		index = 0;
		for (unsigned int i = 0; i < verticesCount; i++, index += 3) {
			float r = mesh->mColors[0][i].r;
			float g = mesh->mColors[0][i].g;
			float b = mesh->mColors[0][i].b;
			vertexColorValues[index + 0] = r;
			vertexColorValues[index + 1] = g;
			vertexColorValues[index + 2] = b;
		}
		env->ReleaseFloatArrayElements(vertexColorArray, vertexColorValues, 0);
		callSetterMethodWithObjectArgument(env, meshObject, "com/assimp4j/data/AiMesh|setVertexColors", vertexColorArray);
	}

	//FACES
	int facesCount = mesh->mNumFaces;
	jintArray facesArray = env->NewIntArray(facesCount * 3);
	jint*facesArrayValues = env->GetIntArrayElements(facesArray, NULL);
	index = 0;
	for (int i = 0; i < facesCount; i++, index += 3) {
		aiFace*face = &(mesh->mFaces[i]);
		int numIndicies = face->mNumIndices;

		facesArrayValues[index + 0] = face->mIndices[0];
		facesArrayValues[index + 1] = face->mIndices[1];
		facesArrayValues[index + 2] = face->mIndices[2];
	}

	env->ReleaseIntArrayElements(facesArray, facesArrayValues, 0);
	callSetterMethodWithObjectArgument(env, meshObject, "com/assimp4j/data/AiMesh|setFaces", facesArray);

	if (mesh->HasBones()) {
		int bonesCount = mesh->mNumBones;
		callSetterMethodWithIntArgument(env, meshObject, "com/assimp4j/data/AiMesh|createBonesArray", bonesCount);
		for (int j = 0; j < bonesCount; j++) {
			aiBone*bone = mesh->mBones[j];
			jobject boneObject = instantiateObject(env, ustring("com/assimp4j/data/AiBone"));
			callSetterMethodWithStringArgument(env, boneObject, "com/assimp4j/data/AiBone|setName", bone->mName.C_Str());
			callSetterMethodWithObjectArgument(env, boneObject, "com/assimp4j/data/AiBone|setOffsetMatrix", convertMatrix4x4ToJavaArray(env, &bone->mOffsetMatrix));
			jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiMesh|setBoneByIndex"));
			env->CallVoidMethod(meshObject, method,j, boneObject);
			setAiBoneVerticesWeight(env, bone, boneObject);
		}
	}
}

void processAnimation(JNIEnv*env, aiAnimation*animation, jobject aiSceneJavaObject) {
	jobject animationObject = instantiateObject(env, ustring("com/assimp4j/data/AiAnimation"));
	callSetterMethodWithObjectArgument(env, aiSceneJavaObject, "com/assimp4j/data/AiScene|addAnimation", animationObject);
	callSetterMethodWithStringArgument(env, animationObject, ustring("com/assimp4j/data/AiAnimation|setName"), animation->mName.C_Str());
	callSetterMethodWithDoubleArgument(env, animationObject, ustring("com/assimp4j/data/AiAnimation|setTicksPerSecond"), animation->mTicksPerSecond);
	callSetterMethodWithDoubleArgument(env, animationObject, ustring("com/assimp4j/data/AiAnimation|setDuration"), animation->mDuration);
	
	for (unsigned int j = 0; j < animation->mNumChannels; j++) {
		aiNodeAnim*channel=animation->mChannels[j];
		jobject animationChannelObject = instantiateObject(env, ustring("com/assimp4j/data/AiAnimationNodeChannel"));
		callSetterMethodWithObjectArgument(env, animationObject, "com/assimp4j/data/AiAnimation|addChannel", animationChannelObject);

		callSetterMethodWithStringArgument(env, animationChannelObject, ustring("com/assimp4j/data/AiAnimationNodeChannel|setNodeName"), channel->mNodeName.C_Str());		
		for (unsigned int i = 0; i < channel->mNumPositionKeys; i++) {
			jobject positionKey = instantiateObject(env, ustring("com/assimp4j/data/AiAnimationPositionKey"));
			aiVectorKey*key=&(channel->mPositionKeys[i]);
			callSetterMethodWithDoubleArgument(env, positionKey, "com/assimp4j/data/AiAnimationPositionKey|setTime", key->mTime);
			callSetterMethodWithObjectArgument(env, positionKey, "com/assimp4j/data/AiAnimationPositionKey|setVector",convertVector3fToJavaArray(env,&key->mValue));
			callSetterMethodWithObjectArgument(env, animationChannelObject, "com/assimp4j/data/AiAnimationNodeChannel|addPositionKey", positionKey);
		}

		for (unsigned int i = 0; i < channel->mNumRotationKeys; i++) {
			jobject rotationKey = instantiateObject(env, ustring("com/assimp4j/data/AiAnimationRotationKey"));
			aiQuatKey*key = &(channel->mRotationKeys[i]);
			callSetterMethodWithDoubleArgument(env, rotationKey, "com/assimp4j/data/AiAnimationRotationKey|setTime", key->mTime);
			callSetterMethodWithObjectArgument(env, rotationKey, "com/assimp4j/data/AiAnimationRotationKey|setQuaternion", convertQuaternionToJavaArray(env, &key->mValue));
			callSetterMethodWithObjectArgument(env, animationChannelObject, "com/assimp4j/data/AiAnimationNodeChannel|addRotationKey", rotationKey);
		}

		for (unsigned int i = 0; i < channel->mNumScalingKeys; i++) {
			jobject scaleKey = instantiateObject(env, ustring("com/assimp4j/data/AiAnimationScaleKey"));
			aiVectorKey*key = &(channel->mScalingKeys[i]);
			callSetterMethodWithDoubleArgument(env, scaleKey, "com/assimp4j/data/AiAnimationScaleKey|setTime", key->mTime);
			callSetterMethodWithObjectArgument(env, scaleKey, "com/assimp4j/data/AiAnimationScaleKey|setVector", convertVector3fToJavaArray(env, &key->mValue));
			callSetterMethodWithObjectArgument(env, animationChannelObject, "com/assimp4j/data/AiAnimationNodeChannel|addScaleKey", scaleKey);
		}
	}
}

jobject processNodes(JNIEnv*env,aiNode*currentNode, jobject parentNodeObject) {
	jobject nodeObject = instantiateObject(env, ustring("com/assimp4j/data/AiNode"));
	callSetterMethodWithStringArgument(env, nodeObject, "com/assimp4j/data/AiNode|setName",currentNode->mName.C_Str());
	
	//mesh indexes
	jintArray meshIndexes = env->NewIntArray(currentNode->mNumMeshes);
	jint*meshIndexesArray=env->GetIntArrayElements(meshIndexes, NULL);
	for (unsigned int i = 0; i < currentNode->mNumMeshes; i++) {
		meshIndexesArray[i] = currentNode->mMeshes[i];
	}
	env->ReleaseIntArrayElements(meshIndexes, meshIndexesArray, NULL);
	callSetterMethodWithObjectArgument(env, nodeObject, "com/assimp4j/data/AiNode|setMeshIndexes",meshIndexes);
	
	//decompose transformation
	aiVector3D positionVector;
	aiVector3D scaleVector;
	aiQuaternion rotation;
	currentNode->mTransformation.Decompose(scaleVector, rotation, positionVector);
	callSetterMethodWithObjectArgument(env, nodeObject, "com/assimp4j/data/AiNode|setPosition",convertVector3fToJavaArray(env,&positionVector));
	callSetterMethodWithObjectArgument(env, nodeObject, "com/assimp4j/data/AiNode|setQuaternion", convertQuaternionToJavaArray(env, &rotation));
	callSetterMethodWithObjectArgument(env, nodeObject, "com/assimp4j/data/AiNode|setScale", convertVector3fToJavaArray(env, &scaleVector));

	//fill metadata properties
	if(currentNode->mMetaData!=NULL){
		int numProperties=currentNode->mMetaData->mNumProperties;
		for (int i = 0; i < numProperties; i++) {
			jstring keyJavaString = env->NewStringUTF(currentNode->mMetaData->mKeys[i].C_Str());
			aiMetadataType metadataType = currentNode->mMetaData->mValues[i].mType;
			void*data = currentNode->mMetaData->mValues[i].mData;

			switch (metadataType) {
			case AI_BOOL: {
				jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiNode|addMetadataBoolKey"));
				env->CallVoidMethod(nodeObject, method, keyJavaString, *((bool*)data));
				break;
			}
			case AI_INT32: {
				jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiNode|addMetadataIntKey"));
				env->CallVoidMethod(nodeObject, method, keyJavaString, *((__int32*)data));
				break;
			}
			case AI_UINT64: {
				jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiNode|addMetadataLongKey"));
				env->CallVoidMethod(nodeObject, method, keyJavaString, *((__int64*)data));
				break;
			}
			case AI_FLOAT: {
				jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiNode|addMetadataFloatKey"));
				env->CallVoidMethod(nodeObject, method, keyJavaString, *((float*)data));
				break;
			}
			case AI_DOUBLE: {
				jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiNode|addMetadataDoubleKey"));
				env->CallVoidMethod(nodeObject, method, keyJavaString, *((double*)data));
				break;
			}
			case AI_AISTRING: {
				jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiNode|addMetadataStringKey"));
				aiString*str = (aiString*)data;
				env->CallVoidMethod(nodeObject, method, keyJavaString, env->NewStringUTF(str->C_Str()));
				break;
			}
			case AI_AIVECTOR3D: {
				jmethodID method = findMethodIgnoreSignature(env, false, ustring("com/assimp4j/data/AiNode|addMetadataVectorKey"));
				aiVector3D*vector = (aiVector3D*)data;
				env->CallVoidMethod(nodeObject, method, keyJavaString, convertVector3fToJavaArray(env, vector));
				break;
			}
			}
		}
	}


	if (parentNodeObject != NULL) {
		callSetterMethodWithObjectArgument(env, nodeObject, "com/assimp4j/data/AiNode|setParent", parentNodeObject);
	}

	jobjectArray childrenArray=env->NewObjectArray(currentNode->mNumChildren,findClass(env,ustring("com/assimp4j/data/AiNode")),NULL);
	for (UINT i = 0; i < currentNode->mNumChildren; i++) {
		jobject childNodeObject = processNodes(env, currentNode->mChildren[i], nodeObject);
		env->SetObjectArrayElement(childrenArray, i, childNodeObject);
	}

	callSetterMethodWithObjectArgument(env, nodeObject, "com/assimp4j/data/AiNode|setChildren", childrenArray);
	return nodeObject;
}

jobject convertScene(JNIEnv*env,aiScene*scene) {
	jobject aiSceneJavaObject = instantiateObject(env, ustring("com/assimp4j/data/AiScene"));

	for (unsigned int i = 0; i < scene->mNumMaterials; i++) {
		aiMaterial*mat = scene->mMaterials[i];
		aiString name;
		aiString texture;
		int isWireframe = 0;
		int isTwoSided = 0;
		float opacity = 1;
		_aiGetMaterialTexture(mat, aiTextureType_DIFFUSE, 0, &texture, NULL, NULL, NULL, NULL, NULL, NULL);
		_aiGetMaterialString(mat, AI_MATKEY_NAME, &name);
		_aiGetMaterialIntegerArray(mat, AI_MATKEY_ENABLE_WIREFRAME, &isWireframe, 0);
		_aiGetMaterialIntegerArray(mat, AI_MATKEY_TWOSIDED, &isTwoSided, 0);
		_aiGetMaterialFloatArray(mat, AI_MATKEY_OPACITY, &opacity, 0);

		jobject matObject = instantiateObject(env, ustring("com/assimp4j/data/AiMaterial"));
		callSetterMethodWithObjectArgument(env, aiSceneJavaObject, "com/assimp4j/data/AiScene|addMaterial", matObject);
		callSetterMethodWithStringArgument(env, matObject, "com/assimp4j/data/AiMaterial|setName", name.C_Str());
		callSetterMethodWithStringArgument(env, matObject, "com/assimp4j/data/AiMaterial|setTexture", texture.C_Str());
		callSetterMethodWithBoolArgument(env, matObject, "com/assimp4j/data/AiMaterial|setIsWireframe", isWireframe>1);
		callSetterMethodWithBoolArgument(env, matObject, "com/assimp4j/data/AiMaterial|setTwoSided", isTwoSided>1);
		callSetterMethodWithFloatArgument(env, matObject, "com/assimp4j/data/AiMaterial|setOpacity", opacity);
	}

	for (unsigned int i = 0; i < scene->mNumMeshes; i++) {
		aiMesh*mesh = scene->mMeshes[i];
		processMesh(env, mesh, aiSceneJavaObject);
	}

	for (unsigned int i = 0; i < scene->mNumAnimations; i++) {
		aiAnimation*animation = scene->mAnimations[i];
		processAnimation(env, animation, aiSceneJavaObject);
	}

	jobject rootNode = processNodes(env, scene->mRootNode, NULL);
	callSetterMethodWithObjectArgument(env, aiSceneJavaObject, "com/assimp4j/data/AiScene|setRootNode", rootNode);
	return aiSceneJavaObject;
}

#ifdef __cplusplus
extern "C" {
#endif
	JNIEXPORT void JNICALL  Java_com_assimp4j_AssImp_init(JNIEnv *env,jobject self, jstring libraryNameJString) {
		try {
			std::wstring path = Java_To_WStr(env, libraryNameJString);
			HMODULE moduleHandle = GetModuleHandle(path.c_str());

			if (moduleHandle == NULL) {
				ustring errorMessage = ustring("Cannot find library [") + ustring(env, libraryNameJString) + "]";
				throwRuntimeException(env, errorMessage.c_str());
			}

			importFunction(env, moduleHandle, "aiImportFile", (void**)&_aiImportFileDll);
			importFunction(env, moduleHandle, "aiImportFileFromMemory", (void**)&_aiImportFileFromMemory);
			importFunction(env, moduleHandle, "aiReleaseImport", (void**)&_aiReleaseImport);
			importFunction(env, moduleHandle, "aiApplyPostProcessing", (void**)&_aiApplyPostProcessing);
			importFunction(env, moduleHandle, "aiIsExtensionSupported", (void**)&_aiIsExtensionSupported);
			importFunction(env, moduleHandle, "aiDecomposeMatrix", (void**)&_aiDecomposeMatrix);
			importFunction(env, moduleHandle, "aiGetMaterialString", (void**)&_aiGetMaterialString);
			importFunction(env, moduleHandle, "aiGetMaterialTexture", (void**)&_aiGetMaterialTexture);
			importFunction(env, moduleHandle, "aiGetMaterialFloatArray", (void**)&_aiGetMaterialFloatArray);
			importFunction(env, moduleHandle, "aiGetMaterialIntegerArray", (void**)&_aiGetMaterialIntegerArray);
			findMethodMethodId = findMethod(env,true, ustring("com/assimp4j/AssImp|findMethod|(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/String;"));
			clearMethodCache();
		}
		catch (std::exception&ex) {
			throwRuntimeException(env, ex.what());
		}
	}

	JNIEXPORT jobject JNICALL  Java_com_assimp4j_AssImp_internalLoadMesh(JNIEnv *env, jobject self, jstring modelPath, jint flags) {
		try {
			ustring path(env, modelPath);
			aiScene*scene = _aiImportFileDll(path.to_std_string().c_str(), (unsigned int)flags);
			jobject aiSceneJavaObject = convertScene(env, scene);
			_aiReleaseImport(scene);
			return aiSceneJavaObject;
		}
		catch (std::exception&ex) {
			throwRuntimeException(env,ex.what());
			return NULL;
		}
	}

	JNIEXPORT jobject JNICALL  Java_com_assimp4j_AssImp_internalLoadMeshFromBuffer(JNIEnv *env, jobject self, jbyteArray byteArray,jstring extensionHintString, jint flags) {
		try {
			jbyte*buffer = env->GetByteArrayElements(byteArray, NULL);
			int length = env->GetArrayLength(byteArray);
			ustring extensionHint("");
			if (extensionHintString != 0) {
				 extensionHint= ustring(env, extensionHintString);
			}
			
			aiScene*scene = _aiImportFileFromMemory((const char*)buffer, length, (unsigned int)flags, extensionHint.empty()?NULL:extensionHint.c_str());
			env->ReleaseByteArrayElements(byteArray, buffer, JNI_ABORT);
			jobject aiSceneJavaObject = convertScene(env, scene);
			_aiReleaseImport(scene);
			return aiSceneJavaObject;
		}
		catch (std::exception&ex) {
			throwRuntimeException(env, ex.what());
			return NULL;
		}
	}
#ifdef __cplusplus
}
#endif
