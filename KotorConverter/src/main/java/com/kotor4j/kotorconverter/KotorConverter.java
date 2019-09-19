package com.kotor4j.kotorconverter;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.kotor4j.entity.*;
import com.kotor4j.enums.NotVisibleWearableType;
import com.kotor4j.kotorconverter.original._2da.Array2da;
import com.kotor4j.kotorconverter.original.chitinkey.ResourceType;
import com.kotor4j.kotorconverter.original.dialog.Tlk;
import com.kotor4j.kotorconverter.original.gff.*;
import com.kotor4j.kotorconverter.original.gff.fields.GffList;
import com.kotor4j.kotorconverter.original.gff.filetypes.*;
import com.kotor4j.kotorconverter.original.lyt.LytFile;
import com.kotor4j.kotorconverter.serializer.KSADumper;
import com.kotor4j.utils.EarcutTriangulator;
import com.kotor4j.utils.TriangulatedMesh;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Dmitry
 */
public class KotorConverter {

    private List<Weapon> weapons = new ArrayList<>();
    private List<Armor> armors = new ArrayList<>();
    private List<Mask> masks = new ArrayList<>();
    private List<Shield> shields = new ArrayList<>();
    private List<Implant> implants = new ArrayList<>();
    private List<Gloves> gloves = new ArrayList<>();
    private List<Belt> belts = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<StaticObject> staticObjects = new ArrayList<>();
    private List<ReferencedEntity> references = new ArrayList<>();
    private List<Waypoint> waypoints = new ArrayList<>();
    private List<Trigger> triggers = new ArrayList<>();
    private List<Placeable> placeables = new ArrayList<>();

    private Map<String, BaseEntity> entitiesById = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new KotorConverter().start(args);
    }

    private void start(String[] args) throws IOException {
        System.out.println("Started KOTOR converter");
        String gameFolder = "h:\\Games\\Games\\SteamLibrary\\steamapps\\common\\swkotor\\";
        String destFolderPath = "g:\\kotor_Extracted\\convertedGame\\";
        String charset = "cp1251";
        Context context = ResourceManager.loadContext(true, gameFolder, charset);
        ResourceManager rm = context.getResourceManager();
        rm.scanWholeResourcesList(false);
        rm.reconstructResourceMap();
        Tlk strings = getTlkFile(context);
        System.out.println("Parsing weapons");
        parseWeapon(context, strings);
        System.out.println("Parsing armors");
        parseArmor(context, strings);
        System.out.println("Parsing masks");
        parseMasks(context, strings);
        System.out.println("Parsing shields");
        parseShields(context, strings);
        System.out.println("Parsing implants");
        parseImplants(context, strings);
        System.out.println("Parsing gloves");
        parseGloves(context, strings);
        System.out.println("Parsing belts");
        parseBelts(context, strings);
        System.out.println("Parsing Levels");
        parseCells(context, strings);
        System.out.println("FinishedParsing");

        writeData(destFolderPath);
    }

    private void writeData(String destFolderPath) throws IOException {
        File destFolder = new File(destFolderPath);
        destFolder.mkdir();
        File destFile = new File(destFolder, "game_archive.ksa");
        System.out.println("Write to file [" + destFile.getAbsolutePath() + "]");
        KSADumper dumper = new KSADumper(destFile);

        dumper.writeObject(armors);
        dumper.writeObject(belts);
        dumper.writeObject(cells);
        dumper.writeObject(gloves);
        dumper.writeObject(implants);
        dumper.writeObject(masks);
        dumper.writeObject(placeables);
        dumper.writeObject(references);
        dumper.writeObject(shields);
        dumper.writeObject(staticObjects);
        dumper.writeObject(triggers);
        dumper.writeObject(waypoints);
        dumper.writeObject(weapons);
        dumper.close();

    }

    private void parseCells(Context context, Tlk strings) throws IOException {
        File rimFolder = new File(context.getResourceManager().getGameFolder(), "modules");
        File[] modulesFiles = rimFolder.listFiles((FileFilter) createModulesFileFilter());
        for (File module : modulesFiles) {
            System.out.println("Parse module [" + module.getAbsolutePath() + "]");
            parseCell(context, strings, module);
        }
    }

    private void parseCell(Context context, Tlk strings, File moduleFile) throws IOException {
        String moduleName = FilenameUtils.getBaseName(moduleFile.getAbsolutePath());
        context.getResourceManager().setCurrentModule(moduleName);
        ResourceType2ResourceListPair resourcesInMainModuleFile = context.getResourceManager().getFileToResourceMap().get(moduleName);

        ResourceRef staticInfoResourceRef = resourcesInMainModuleFile.getResourcesForType(ResourceType.ARE).get(0);
        ResourceRef dynamicInfoResourceRef = resourcesInMainModuleFile.getResourcesForType(ResourceType.GIT).get(0);
        ResourceRef ifoFileResourceRef = resourcesInMainModuleFile.getResourcesForType(ResourceType.IFO).get(0);
        String moduleInternalName = dynamicInfoResourceRef.getName();
        LytFile layoutInfo = findLytFileForModule(context, moduleInternalName);

        GffStructure staticCellInfo = ((AreGff) context.getResourceManager().getConvertedResource(staticInfoResourceRef)).getGff().getRoot();
        GffStructure dynamicCellInfo = ((GitGff) context.getResourceManager().getConvertedResource(dynamicInfoResourceRef)).getGff().getRoot();
        GffStructure ifoFileCellInfo = ((IfoGff) context.getResourceManager().getConvertedResource(ifoFileResourceRef)).getGff().getRoot();

        Cell cell = new Cell();
        cell.setEntityId("cell_" + moduleName);
        addEntityIfSuchIdNotExists(cell);
        //fill map info
        GffStructure mapStructure = staticCellInfo.get("Map").getValue().getAsStructure();
        cell.setMapPoint1X(mapStructure.get("MapPt1X").getValue().getAsFloat());
        cell.setMapPoint1Y(mapStructure.get("MapPt1Y").getValue().getAsFloat());
        cell.setMapPoint2X(mapStructure.get("MapPt2X").getValue().getAsFloat());
        cell.setMapPoint2Y(mapStructure.get("MapPt2Y").getValue().getAsFloat());
        cell.setMapWorldPoint1X(mapStructure.get("WorldPt1X").getValue().getAsFloat());
        cell.setMapWorldPoint1Y(mapStructure.get("WorldPt1Y").getValue().getAsFloat());
        cell.setMapWorldPoint2X(mapStructure.get("WorldPt2X").getValue().getAsFloat());
        cell.setMapWorldPoint2Y(mapStructure.get("WorldPt2Y").getValue().getAsFloat());

        try {
            String mapTexture = "lbl_map" + moduleInternalName;
            ensureTexturePresent(mapTexture, context);
            cell.setMapTexture(mapTexture);
        } catch (Exception ex) {
            System.out.println("\tWARNING: module [" + moduleInternalName + "] [" + moduleName + "] does not have map");
        }
        cell.setGrassTextureName(staticCellInfo.get("Grass_TexName").getValue().getAsStringValue());
        if (!cell.getGrassTextureName().isEmpty()) {
            ensureTexturePresent(cell.getGrassTextureName(), context);
        }

        cell.setUnescapable(staticCellInfo.get("Unescapable").getValue().getAsInt() > 0);
        cell.setTag(staticCellInfo.get("Tag").getValue().getAsStringValue());
        List<String> onEnterScript = new ArrayList<>();
        addScriptNoDuplicate(onEnterScript, getScriptIdByScriptName(ifoFileCellInfo.get("Mod_OnModLoad").getValue().getAsStringValue()));
        addScriptNoDuplicate(onEnterScript, getScriptIdByScriptName(ifoFileCellInfo.get("Mod_OnClientEntr").getValue().getAsStringValue()));
        addScriptNoDuplicate(onEnterScript, getScriptIdByScriptName(staticCellInfo.get("OnEnter").getValue().getAsStringValue()));
        cell.setOnEnterScriptRefId(onEnterScript.toArray(new String[onEnterScript.size()]));
        List<String> onExitScript = new ArrayList<>();
        addScriptNoDuplicate(onExitScript, getScriptIdByScriptName(staticCellInfo.get("OnExit").getValue().getAsStringValue()));
        addScriptNoDuplicate(onExitScript, getScriptIdByScriptName(ifoFileCellInfo.get("Mod_OnClientLeav").getValue().getAsStringValue()));
        cell.setOnExitScriptRefId(onExitScript.toArray(new String[onExitScript.size()]));

        cell.setOnUserDefinedEventScriptRefId(getScriptIdByScriptName(staticCellInfo.get("OnUserDefined").getValue().getAsStringValue()));
        cell.setOnHeartbeatScriptRefId(getScriptIdByScriptName(staticCellInfo.get("OnHeartbeat").getValue().getAsStringValue()));
        cell.setOnItemAquiredEventScriptRefId(getScriptIdByScriptName(ifoFileCellInfo.get("Mod_OnAcquirItem").getValue().getAsStringValue()));
        cell.setTitle(staticCellInfo.get("Name").getValue().getAsString(strings));

        convertStatic(layoutInfo, context, cell);
        convertWaypoints(dynamicCellInfo, strings, cell);
        convertTriggers(dynamicCellInfo, context, cell);
        convertPlaceables(dynamicCellInfo, context, cell, strings, moduleName);
        cells.add(cell);
    }

    private void convertPlaceables(GffStructure dynamicCellInfo, Context context, Cell cell, Tlk strings, String moduleName) throws IOException {
        Array2da placeables2DA = get2daArray(context, "placeables");
        GffList placeablesGff = (GffList) dynamicCellInfo.get("Placeable List").getValue();
        for (int i = 0; i < placeablesGff.size(); i++) {
            GffStructure placeableGff = placeablesGff.getValue()[i];
            String templateRef = placeableGff.get("TemplateResRef").getValue().getAsStringValue();
            Map<ResourceType, ResourceRef> uttFileList = context.getResourceManager().getResourceRefByName(templateRef);
            if (uttFileList.isEmpty()) {
                throw new IllegalStateException("Cannot find template for trigger [" + templateRef + ".utt]");
            }
            try {
                float bearing = placeableGff.get("Bearing").getValue().getAsFloat();
                Quaternion quaternion = new Quaternion().fromAngleAxis(bearing, Vector3f.UNIT_Y);
                float X = placeableGff.get("X").getValue().getAsFloat();
                float Y = placeableGff.get("Y").getValue().getAsFloat();
                float Z = placeableGff.get("Z").getValue().getAsFloat();
                ResourceRef placeableTemplate = uttFileList.get(ResourceType.UTP);

                UtpGff utpGff = (UtpGff) context.getResourceManager().getConvertedResource(placeableTemplate);
                GffStructure utpGffStructure = utpGff.getGff().getRoot();
                Placeable placeable = new Placeable();
                placeable.setEntityId("placeable_" + moduleName + "_" + templateRef);
                placeable.setTag(utpGffStructure.get("Tag").getValue().getAsStringValue());
                placeable.setAnimation(null);//TODO
                placeable.setCurrentHealth(utpGffStructure.get("CurrentHP").getValue().getAsInt());
                placeable.setMaxHealth(utpGffStructure.get("HP").getValue().getAsInt());
                if (utpGffStructure.containsField("Min1HP")) {
                    placeable.setMinHealth(utpGffStructure.get("Min1HP").getValue().getAsInt());
                }
                placeable.setDialogId(utpGffStructure.get("Conversation").getValue().getAsStringValue());
                placeable.setDialogInteruptible(utpGffStructure.get("Interruptable").getValue().getAsBoolean());
                if (utpGffStructure.containsField("Static")) {
                    placeable.setIsStatic(utpGffStructure.get("Static").getValue().getAsBoolean());
                } else {
                    placeable.setIsStatic(true);
                }

                int appearanceIndex = utpGffStructure.get("Appearance").getValue().getAsInt();
                String modelPath = placeables2DA.getColumnValue("modelname", appearanceIndex);
                ensureModelPresent(modelPath.toLowerCase(), context);
                placeable.setModelPath(modelPath);
                placeable.setOnActivateScript(getScriptIdByScriptName(utpGffStructure.get("OnUsed").getValue().getAsStringValue()));
                placeable.setOnAttackedScript(getScriptIdByScriptName(utpGffStructure.get("OnMeleeAttacked").getValue().getAsStringValue()));
                placeable.setOnClosedScript(getScriptIdByScriptName(utpGffStructure.get("OnClosed").getValue().getAsStringValue()));
                placeable.setOnDeathScript(getScriptIdByScriptName(utpGffStructure.get("OnDeath").getValue().getAsStringValue()));
                placeable.setOnHeartbeatScript(getScriptIdByScriptName(utpGffStructure.get("OnHeartbeat").getValue().getAsStringValue()));
                placeable.setOnOpenScript(getScriptIdByScriptName(utpGffStructure.get("OnOpen").getValue().getAsStringValue()));
                placeable.setOnSpellAtScript(getScriptIdByScriptName(utpGffStructure.get("OnSpellCastAt").getValue().getAsStringValue()));
                placeable.setOnUserDefinedEvent(getScriptIdByScriptName(utpGffStructure.get("OnUserDefined").getValue().getAsStringValue()));
                placeable.setTitle(utpGffStructure.get("LocName").getValue().getAsString(strings));
                placeable.setDescription(utpGffStructure.get("Description").getValue().getAsString(strings));
                placeables.add(placeable);

                PositionalReferencedEntity reference = new PositionalReferencedEntity("ref:" + cell.getEntityId() + "->" + placeable.getEntityId());
                reference.setDestEntityId(cell.getEntityId());
                reference.setEntityId(placeable.getEntityId());
                reference.setPosition(X, Y, Z);
                reference.setQuaternionRotation(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.getW());
                reference.setDestSubreferenceType(ReferencedEntity.PLACEABLES_OBJECTS);
                references.add(reference);
            } catch (Exception ex) {
                throw new RuntimeException("Error in placeable [" + templateRef + "]", ex);
            }
        }
    }

    private void convertTriggers(GffStructure dynamicCellInfo, Context context, Cell cell) throws IOException, IllegalStateException {
        GffList triggersGff = (GffList) dynamicCellInfo.get("TriggerList").getValue();
        for (int i = 0; i < triggersGff.size(); i++) {
            GffStructure triggerGff = triggersGff.getValue()[i];
            String templateRef = triggerGff.get("TemplateResRef").getValue().getAsStringValue();
            float x = triggerGff.get("XPosition").getValue().getAsFloat();
            float y = triggerGff.get("YPosition").getValue().getAsFloat();
            float z = triggerGff.get("ZPosition").getValue().getAsFloat();
            float xOrientation = triggerGff.get("XOrientation").getValue().getAsFloat();
            float yOrientation = triggerGff.get("YOrientation").getValue().getAsFloat();
            float zOrientation = triggerGff.get("ZOrientation").getValue().getAsFloat();
            TriangulatedMesh triangulatedMesh = parseTriggersGeometry((GffList) triggerGff.get("Geometry").getValue());
            Map<ResourceType, ResourceRef> uttFileList = context.getResourceManager().getResourceRefByName(templateRef);
            if (uttFileList.isEmpty()) {
                throw new IllegalStateException("Cannot find template for trigger [" + templateRef + ".utt]");
            }
            ResourceRef triggerTemplate = uttFileList.get(ResourceType.UTT);

            UttGff uttGff = (UttGff) context.getResourceManager().getConvertedResource(triggerTemplate);
            GffStructure triggerUttGffStructure = uttGff.getGff().getRoot();
            String scriptHeartbeatRefId = getScriptIdByScriptName(triggerUttGffStructure.get("ScriptHeartbeat").getValue().getAsStringValue());
            String scriptOnEnterRefId = getScriptIdByScriptName(triggerUttGffStructure.get("ScriptOnEnter").getValue().getAsStringValue());
            String scriptOnExitRefId = getScriptIdByScriptName(triggerUttGffStructure.get("ScriptOnExit").getValue().getAsStringValue());

            Trigger trigger = new Trigger();
            trigger.setEntityId("trigger_" + templateRef);
            trigger.setX(x);
            trigger.setY(y);
            trigger.setZ(z);
            trigger.setxOrientation(xOrientation);
            trigger.setyOrientation(yOrientation);
            trigger.setzOrientation(zOrientation);
            trigger.setScriptHeartbeatRefId(scriptHeartbeatRefId);
            trigger.setScriptOnEnterRefId(scriptOnEnterRefId);
            trigger.setScriptOnExitRefId(scriptOnExitRefId);
            trigger.setVertices(triangulatedMesh.getData());
            trigger.setIndicies(triangulatedMesh.getIndicies());
            addEntityIfSuchIdNotExists(trigger);
            triggers.add(trigger);

            ReferencedEntity reference = new ReferencedEntity("ref:" + cell.getEntityId() + "->" + trigger.getEntityId());
            reference.setDestEntityId(cell.getEntityId());
            reference.setEntityId(trigger.getEntityId());
            reference.setDestSubreferenceType(ReferencedEntity.TRIGGERS_OBJECTS);
            references.add(reference);
        }
    }

    private void convertWaypoints(GffStructure dynamicCellInfo, Tlk strings, Cell cell) {
        //waypoints
        GffList waypointsGff = (GffList) dynamicCellInfo.get("WaypointList").getValue();
        for (int i = 0; i < waypointsGff.size(); i++) {
            GffStructure waypointGff = waypointsGff.getValue()[i];
            String description = waypointGff.get("Description").getValue().getAsString(strings);
            boolean hasMapNote = waypointGff.get("HasMapNote").getValue().getAsInt() > 0;
            String mapNote = null;
            if (hasMapNote) {
                mapNote = waypointGff.get("MapNote").getValue().getAsString(strings);
            }
            boolean mapNoteEnabled = waypointGff.get("MapNoteEnabled").getValue().getAsInt() > 0;
            String tag = waypointGff.get("Tag").getValue().getAsStringValue();
            float x = waypointGff.get("XPosition").getValue().getAsFloat();
            float y = waypointGff.get("YPosition").getValue().getAsFloat();
            float z = waypointGff.get("ZPosition").getValue().getAsFloat();
            float xOrientation = waypointGff.get("XOrientation").getValue().getAsFloat();
            float yOrientation = waypointGff.get("YOrientation").getValue().getAsFloat();
            String templateResRef = waypointGff.get("TemplateResRef").getValue().getAsStringValue();
            String refId = "waypoint_" + templateResRef;

            Waypoint waypoint = new Waypoint();
            waypoint.setEntityId(refId);
            waypoint.setX(x);
            waypoint.setY(y);
            waypoint.setZ(z);
            waypoint.setxOrientation(xOrientation);
            waypoint.setyOrientation(yOrientation);
            waypoint.setDescription(description);
            waypoint.setMapNote(mapNote);
            waypoint.setMapNoteEnabled(mapNoteEnabled);
            waypoint.setTag(tag);
            addEntityIfSuchIdNotExists(waypoint);
            waypoints.add(waypoint);

            ReferencedEntity reference = new ReferencedEntity("ref:" + cell.getEntityId() + "->" + refId);
            reference.setDestEntityId(cell.getEntityId());
            reference.setEntityId(waypoint.getEntityId());
            reference.setDestSubreferenceType(ReferencedEntity.WAYPOINT_OBJECTS);
            references.add(reference);
        }
    }

    private void convertStatic(LytFile layoutInfo, Context context, Cell cell) {
        //static objects
        for (Map<String, Object> room : layoutInfo.getRooms()) {
            String modelName = (String) room.get("model");
            float x = (float) room.get("x");
            float y = (float) room.get("y");
            float z = (float) room.get("z");
            String modelNameLower = modelName.toLowerCase();
            System.out.println("\tCheck room [" + modelNameLower + "]");
            if (modelNameLower.equals("****")) {
                continue;
            }
            ensureModelPresent(modelNameLower, context);
            String staticObjectEntityId = createStaticObject(modelName);
            PositionalReferencedEntity reference = new PositionalReferencedEntity("ref:" + cell.getEntityId() + "->" + staticObjectEntityId);
            reference.setEntityId(staticObjectEntityId);
            reference.setDestEntityId(cell.getEntityId());
            reference.setDestSubreferenceType(ReferencedEntity.STATIC_OBJECTS);
            reference.setPosition(x, y, z);
            reference.setQuaternionRotation(0f, 0f, 0f, 1f);
            references.add(reference);
        }
    }

    private TriangulatedMesh parseTriggersGeometry(GffList geometryList) {
        int size = geometryList.size();
        float[] floatComponents = new float[size * 3];
        int index = 0;
        for (int i = 0; i < size; i++) {
            GffStructure point = geometryList.getValue()[i];
            floatComponents[index + 0] = point.get("PointX").getValue().getAsFloat();
            floatComponents[index + 1] = point.get("PointY").getValue().getAsFloat();
            floatComponents[index + 2] = point.get("PointZ").getValue().getAsFloat();
            index += 3;
        }

        return EarcutTriangulator.earcut(floatComponents);
    }

    private String createStaticObject(String modelPath) {
        String staticEntityId = "static_" + modelPath;
        if (entitiesById.containsKey(staticEntityId)) {
            Object obj = entitiesById.get(staticEntityId);
            if (obj instanceof StaticObject) {
                return staticEntityId;
            } else {
                throw new IllegalArgumentException("Entity with name [" + staticEntityId + "] already exists and has type [" + obj.getClass().getSimpleName() + "]");
            }
        }

        StaticObject staticObject = new StaticObject();
        staticObject.setEntityId(staticEntityId);
        staticObject.setModelPath(modelPath);
        staticObjects.add(staticObject);
        entitiesById.put(staticEntityId, staticObject);
        return staticEntityId;
    }

    private void addScriptNoDuplicate(List<String> scripts, String script) {
        if (script.isEmpty()) {
            return;
        }
        if (scripts.contains(script)) {
            return;
        }
        scripts.add(script);
    }

    private String getScriptIdByScriptName(String scriptName) {
        System.out.println("\tTODO: extract script [" + scriptName + "]");
        return scriptName;
    }

    private void addEntityIfSuchIdNotExists(BaseEntity entity) {
        if (entitiesById.containsKey(entity.getEntityId())) {
            BaseEntity oldEntity = entitiesById.get(entity.getEntityId());
            throw new IllegalArgumentException("Entity with following id [" + entity.getEntityId() + "] already exists. Old entity [" + oldEntity.getClass().getSimpleName() + "]");
        }
    }

    private LytFile findLytFileForModule(Context context, String moduleName) throws IOException {
        List<ResourceRef> refs = context.getResourceManager().getFileToResourceMap().get("layouts").getResourcesForType(ResourceType.LYT);
        for (ResourceRef resourceRef : refs) {
            if (resourceRef.getName().equals(moduleName)) {
                return (LytFile) context.getResourceManager().getConvertedResource(resourceRef);
            }
        }

        throw new IllegalArgumentException("Cannot find lyt file for module [" + moduleName + "]");
    }

    private void baseParseUti(Context context, ProcessUTI itemProcess) throws IOException {
        ResourceManager rm = context.getResourceManager();
        Array2da baseItems = get2daArray(context, "baseitems");
        for (String file : rm.fileToResourceMap.keySet()) {
            ResourceType2ResourceListPair resources = rm.fileToResourceMap.get(file);
            List<ResourceRef> resRefs = resources.getResourcesForType(ResourceType.UTI);
            if (resRefs != null) {
                for (ResourceRef resRef : resRefs) {
                    UtiGff utiGff = (UtiGff) rm.getConvertedResource(resRef);
                    try {
                        itemProcess.apply(baseItems, utiGff);
                    } catch (Exception ex) {
                        throw new RuntimeException("Error while converting uti file [" + resRef.getName() + "]", ex);
                    }
                }
            }
        }
    }

    private void parseWeapon(Context context, Tlk strings) throws IOException {
        baseParseUti(context, (baseItems, utiGff) -> {
            Weapon weapon = convertWeapon(baseItems, utiGff, strings, context);
            if (weapon != null) {
                weapons.add(weapon);
            }
        });
    }

    private void parseArmor(Context context, Tlk strings) throws IOException {
        baseParseUti(context, (baseItems, utiGff) -> {
            Armor armor = convertArmor(baseItems, utiGff, strings, context);
            if (armor != null) {
                armors.add(armor);
            }
        });
    }

    private void parseMasks(Context context, Tlk strings) throws IOException {
        baseParseUti(context, (baseItems, utiGff) -> {
            Mask armor = convertMask(baseItems, utiGff, strings, context);
            if (armor != null) {
                masks.add(armor);
            }
        });
    }

    private void parseShields(Context context, Tlk strings) throws IOException {
        baseParseUti(context, (baseItems, utiGff) -> {
            Shield shield = convertShield(baseItems, utiGff, strings, context);
            if (shield != null) {
                shields.add(shield);
            }
        });
    }

    private void parseImplants(Context context, Tlk strings) throws IOException {
        baseParseUti(context, (baseItems, utiGff) -> {
            Implant implant = convertImplant(baseItems, utiGff, strings, context);
            if (implant != null) {
                implants.add(implant);
            }
        });
    }

    private void parseGloves(Context context, Tlk strings) throws IOException {
        baseParseUti(context, (baseItems, utiGff) -> {
            Gloves glove = convertGloves(baseItems, utiGff, strings, context);
            if (glove != null) {
                gloves.add(glove);
            }
        });
    }

    private void parseBelts(Context context, Tlk strings) throws IOException {
        baseParseUti(context, (baseItems, utiGff) -> {
            Belt belt = convertBelt(baseItems, utiGff, strings, context);
            if (belt != null) {
                belts.add(belt);
            }
        });
    }

    private Weapon convertWeapon(Array2da baseItems, UtiGff weaponGff, Tlk strings, Context context) {
        int baseItemIndex = weaponGff.getGff().getRoot().get("BaseItem").getValue().getAsInt();
        String baseItemName = baseItems.getColumnValue("label", baseItemIndex);
        BaseItemLabels.WeaponBaseInfo baseItemInfo = BaseItemLabels.weaponLabelInfo.get(baseItemName);
        if (baseItemInfo == null) {
            return null;
        }

        GffStructure gff = weaponGff.getGff().getRoot();
        Weapon weapon = new Weapon();
        weapon.setEntityId(gff.get("TemplateResRef").getValue().getAsStringValue());
        weapon.setTitle(gff.get("LocalizedName").getValue().getAsString(strings));
        weapon.setDescription(gff.get("DescIdentified").getValue().getAsString(strings));
        String model = baseItems.getColumnValue("defaultmodel", baseItemIndex).toLowerCase();
        String iconPath = "i" + model;
        weapon.setIconPath(iconPath);
        weapon.setTag(gff.get("Tag").getValue().getAsStringValue());
        weapon.setPrice(gff.get("Cost").getValue().getAsInt());
        weapon.setQuestItem((gff.get("Plot").getValue().getAsInt() != 0));
        weapon.setModelPath(model);
        weapon.setMaxDie(Integer.parseInt(baseItems.getColumnValue("dietoroll", baseItemIndex)));
        weapon.setNumDice(Integer.parseInt(baseItems.getColumnValue("numdice", baseItemIndex)));
        weapon.setRange(Integer.parseInt(baseItems.getColumnValue("maxrange", baseItemIndex)));
        weapon.setCanUpgrade(canUpgrade(weaponGff.getGff()));
        weapon.setWeaponType(baseItemInfo.getWeaponType());

        ensureIconPresent(iconPath, context);
        ensureModelPresent(model, context);

        return weapon;
    }

    private Armor convertArmor(Array2da baseItems, UtiGff wearableGff, Tlk strings, Context context) {
        int baseItemIndex = wearableGff.getGff().getRoot().get("BaseItem").getValue().getAsInt();
        String baseItemName = baseItems.getColumnValue("label", baseItemIndex);
        BaseItemLabels.WearableBaseInfo baseItemInfo = BaseItemLabels.wearableLabelInfo.get(baseItemName);
        if (baseItemInfo == null || baseItemInfo.getWearableType() != NotVisibleWearableType.Armor) {
            return null;
        }

        GffStructure gff = wearableGff.getGff().getRoot();
        Armor wearable = new Armor();
        wearable.setEntityId(gff.get("TemplateResRef").getValue().getAsStringValue());
        wearable.setTitle(gff.get("LocalizedName").getValue().getAsString(strings));
        wearable.setDescription(gff.get("DescIdentified").getValue().getAsString(strings));
        String itemClass = baseItems.getColumnValue("itemclass", baseItemIndex).toLowerCase();
        if (!gff.containsField("BodyVariation")) {
            return null;//for some reason 'w_bstrcrbn' has closing base item
        }
        int bodyVar = gff.get("BodyVariation").getValue().getAsInt();
        int textureVar = gff.get("TextureVar").getValue().getAsInt();

        char bodyVarChar = (char) ('A' + bodyVar - 1);
        String maleTexture = "PMB" + bodyVarChar + String.format("%02d", textureVar);
        String femaleTexture = "PFB" + bodyVarChar + String.format("%02d", textureVar);

        try {
            ensureTexturePresent(maleTexture, context);
        } catch (IllegalStateException ex) {
            try {
                maleTexture = "PMB" + bodyVarChar + "M" + String.format("%02d", textureVar);
                ensureTexturePresent(maleTexture, context);
            } catch (IllegalStateException ex2) {
                System.out.println("Cannot find texture [" + maleTexture + "] for item [" + baseItemName + "]");
            }
        }

        try {
            ensureTexturePresent(femaleTexture, context);
        } catch (IllegalStateException ex) {
            try {
                femaleTexture = "PFB" + bodyVarChar + "M" + String.format("%02d", textureVar);
                ensureTexturePresent(femaleTexture, context);
            } catch (IllegalStateException ex2) {
                System.out.println("Cannot find texture [" + femaleTexture + "] for item [" + baseItemName + "]");
            }
        }

        wearable.setMaleModelTexture(maleTexture);
        wearable.setFemaleModelTexture(femaleTexture);

        String iconPath = "i" + itemClass + "_0" + String.format("%02d", textureVar);
        ensureIconPresent(iconPath, context);
        wearable.setIconPath(iconPath);

        wearable.setTag(gff.get("Tag").getValue().getAsStringValue());
        wearable.setPrice(gff.get("Cost").getValue().getAsInt());
        wearable.setQuestItem((gff.get("Plot").getValue().getAsInt() != 0));

        wearable.setCanUpgrade(canUpgrade(wearableGff.getGff()));

        wearable.setArmorClass(Integer.parseInt(baseItems.getColumnValue("baseac", baseItemIndex)));

        String modelMale = "PMB" + bodyVarChar + "M";
        ensureModelPresent(modelMale.toLowerCase(), context);
        wearable.setMaleModelPath(modelMale);

        String modelFemale = "PFB" + bodyVarChar + "M";
        ensureModelPresent(modelFemale.toLowerCase(), context);
        wearable.setFemaleModelPath(modelFemale);
        return wearable;
    }

    private Mask convertMask(Array2da baseItems, UtiGff wearableGff, Tlk strings, Context context) {
        int baseItemIndex = wearableGff.getGff().getRoot().get("BaseItem").getValue().getAsInt();
        String baseItemName = baseItems.getColumnValue("label", baseItemIndex);
        BaseItemLabels.WearableBaseInfo baseItemInfo = BaseItemLabels.wearableLabelInfo.get(baseItemName);
        if (baseItemInfo == null || baseItemInfo.getWearableType() != NotVisibleWearableType.Mask) {
            return null;
        }

        GffStructure gff = wearableGff.getGff().getRoot();
        Mask mask = new Mask();
        mask.setEntityId(gff.get("TemplateResRef").getValue().getAsStringValue());
        mask.setTitle(gff.get("LocalizedName").getValue().getAsString(strings));
        mask.setDescription(gff.get("DescIdentified").getValue().getAsString(strings));
        //String itemClass = baseItems.getColumnValue("itemclass", baseItemIndex).toLowerCase();
        int modelVar = gff.get("ModelVariation").getValue().getAsInt();

        //String texture = "PMB" + bodyVarChar + String.format("%02d", textureVar);
        //ensureTexturePresent(maleTexture, context);
        //mask.setModelTexture(maleTexture);
        String maskName = ("i_" + baseItemName + "_" + String.format("%03d", modelVar)).toLowerCase();
        String iconPath = "i" + maskName;
        ensureIconPresent(iconPath, context);
        mask.setIconPath(iconPath);

        mask.setTag(gff.get("Tag").getValue().getAsStringValue());
        mask.setPrice(gff.get("Cost").getValue().getAsInt());
        mask.setQuestItem((gff.get("Plot").getValue().getAsInt() != 0));

        mask.setCanUpgrade(canUpgrade(wearableGff.getGff()));

        mask.setArmorClass(Integer.parseInt(baseItems.getColumnValue("baseac", baseItemIndex)));

        ensureModelPresent(maskName, context);
        mask.setModelPath(maskName);
        return mask;
    }

    private Shield convertShield(Array2da baseItems, UtiGff wearableGff, Tlk strings, Context context) {
        int baseItemIndex = wearableGff.getGff().getRoot().get("BaseItem").getValue().getAsInt();
        String baseItemName = baseItems.getColumnValue("label", baseItemIndex);
        BaseItemLabels.WearableBaseInfo baseItemInfo = BaseItemLabels.wearableLabelInfo.get(baseItemName);
        if (baseItemInfo == null || baseItemInfo.getWearableType() != NotVisibleWearableType.Shield) {
            return null;
        }

        GffStructure gff = wearableGff.getGff().getRoot();
        Shield shield = new Shield();
        shield.setEntityId(gff.get("TemplateResRef").getValue().getAsStringValue());
        shield.setTitle(gff.get("LocalizedName").getValue().getAsString(strings));
        shield.setDescription(gff.get("DescIdentified").getValue().getAsString(strings));
        String itemClass = baseItems.getColumnValue("itemclass", baseItemIndex).toLowerCase();

        int modelVar = gff.get("ModelVariation").getValue().getAsInt();
        String iconPath = "i" + itemClass + "_0" + String.format("%02d", modelVar);
        ensureIconPresent(iconPath, context);
        shield.setIconPath(iconPath);

        shield.setTag(gff.get("Tag").getValue().getAsStringValue());
        shield.setPrice(gff.get("Cost").getValue().getAsInt());
        shield.setQuestItem((gff.get("Plot").getValue().getAsInt() != 0));

        shield.setMaxUses(gff.get("Charges").getValue().getAsInt());
        shield.setNumberOfUsing(gff.get("Charges").getValue().getAsInt());

        return shield;
    }

    private Implant convertImplant(Array2da baseItems, UtiGff wearableGff, Tlk strings, Context context) {
        int baseItemIndex = wearableGff.getGff().getRoot().get("BaseItem").getValue().getAsInt();
        String baseItemName = baseItems.getColumnValue("label", baseItemIndex);
        BaseItemLabels.WearableBaseInfo baseItemInfo = BaseItemLabels.wearableLabelInfo.get(baseItemName);
        if (baseItemInfo == null || baseItemInfo.getWearableType() != NotVisibleWearableType.Shield) {
            return null;
        }

        GffStructure gff = wearableGff.getGff().getRoot();
        Implant implant = new Implant();
        implant.setEntityId(gff.get("TemplateResRef").getValue().getAsStringValue());
        implant.setTitle(gff.get("LocalizedName").getValue().getAsString(strings));
        implant.setDescription(gff.get("DescIdentified").getValue().getAsString(strings));
        String itemClass = baseItems.getColumnValue("itemclass", baseItemIndex).toLowerCase();

        int modelVar = gff.get("ModelVariation").getValue().getAsInt();
        String iconPath = "i" + itemClass + "_0" + String.format("%02d", modelVar);
        ensureIconPresent(iconPath, context);
        implant.setIconPath(iconPath);

        implant.setTag(gff.get("Tag").getValue().getAsStringValue());
        implant.setPrice(gff.get("Cost").getValue().getAsInt());
        implant.setQuestItem((gff.get("Plot").getValue().getAsInt() != 0));

        return implant;
    }

    private Gloves convertGloves(Array2da baseItems, UtiGff wearableGff, Tlk strings, Context context) {
        int baseItemIndex = wearableGff.getGff().getRoot().get("BaseItem").getValue().getAsInt();
        String baseItemName = baseItems.getColumnValue("label", baseItemIndex);
        BaseItemLabels.WearableBaseInfo baseItemInfo = BaseItemLabels.wearableLabelInfo.get(baseItemName);
        if (baseItemInfo == null || baseItemInfo.getWearableType() != NotVisibleWearableType.Gloves) {
            return null;
        }

        GffStructure gff = wearableGff.getGff().getRoot();
        Gloves gloves = new Gloves();
        gloves.setEntityId(gff.get("TemplateResRef").getValue().getAsStringValue());
        gloves.setTitle(gff.get("LocalizedName").getValue().getAsString(strings));
        gloves.setDescription(gff.get("DescIdentified").getValue().getAsString(strings));
        String itemClass = baseItems.getColumnValue("itemclass", baseItemIndex).toLowerCase();

        int modelVar = gff.get("ModelVariation").getValue().getAsInt();
        String iconPath = "i" + itemClass + "_0" + String.format("%02d", modelVar);
        ensureIconPresent(iconPath, context);
        gloves.setIconPath(iconPath);

        gloves.setTag(gff.get("Tag").getValue().getAsStringValue());
        gloves.setPrice(gff.get("Cost").getValue().getAsInt());
        gloves.setQuestItem((gff.get("Plot").getValue().getAsInt() != 0));

        return gloves;
    }

    private Belt convertBelt(Array2da baseItems, UtiGff wearableGff, Tlk strings, Context context) {
        int baseItemIndex = wearableGff.getGff().getRoot().get("BaseItem").getValue().getAsInt();
        String baseItemName = baseItems.getColumnValue("label", baseItemIndex);
        BaseItemLabels.WearableBaseInfo baseItemInfo = BaseItemLabels.wearableLabelInfo.get(baseItemName);
        if (baseItemInfo == null || baseItemInfo.getWearableType() != NotVisibleWearableType.Gloves) {
            return null;
        }

        GffStructure gff = wearableGff.getGff().getRoot();
        Belt belt = new Belt();
        belt.setEntityId(gff.get("TemplateResRef").getValue().getAsStringValue());
        belt.setTitle(gff.get("LocalizedName").getValue().getAsString(strings));
        belt.setDescription(gff.get("DescIdentified").getValue().getAsString(strings));
        String itemClass = baseItems.getColumnValue("itemclass", baseItemIndex).toLowerCase();

        int modelVar = gff.get("ModelVariation").getValue().getAsInt();
        String iconPath = "i" + itemClass + "_0" + String.format("%02d", modelVar);
        ensureIconPresent(iconPath, context);
        belt.setIconPath(iconPath);

        belt.setTag(gff.get("Tag").getValue().getAsStringValue());
        belt.setPrice(gff.get("Cost").getValue().getAsInt());
        belt.setQuestItem((gff.get("Plot").getValue().getAsInt() != 0));

        return belt;
    }

    private boolean canUpgrade(Gff item) {
        GffList gffList = (GffList) item.getRoot().get("PropertiesList").getValue();
        for (GffStructure prop : gffList.getValue()) {
            if (prop.containsField("UpgradeType")) {
                return true;
            }
        }
        return false;
    }

    private void ensureTexturePresent(String name, Context context) {
        Map<ResourceType, ResourceRef> resource = context.getResourceManager().getResourceRefByName(name.toLowerCase());
        if (resource == null || !(resource.containsKey(ResourceType.TPC) || resource.containsKey(ResourceType.TGA))) {
            throw new IllegalStateException("Cannot find texture [" + name + "]");
        }
    }

    private void ensureIconPresent(String name, Context context) {
        Map<ResourceType, ResourceRef> resource = context.getResourceManager().getResourceRefByName(name);
        if (resource == null || !resource.containsKey(ResourceType.TPC)) {
            throw new IllegalStateException("Cannot find icon [" + name + "]");
        }
    }

    private void ensureModelPresent(String name, Context context) {
        Map<ResourceType, ResourceRef> resource = context.getResourceManager().getResourceRefByName(name);
        if (!resource.containsKey(ResourceType.MDL)) {
            throw new IllegalStateException("Cannot find model [" + name + "]");
        }
    }

    private Array2da get2daArray(Context context, String name) throws IOException {
        Array2da table = (Array2da) getResource(context, name, ResourceType._2DA);
        return table;
    }

    private Tlk getTlkFile(Context context) throws IOException {
        return (Tlk) getResource(context, "dialog", ResourceType.TLK);
    }

    private Object getResource(Context context, String name, ResourceType resourceType) throws IOException {
        ResourceManager rm = context.getResourceManager();
        Map<ResourceType, ResourceRef> r = rm.getResourceRefByName(name);
        if (r == null) {
            throw new IllegalStateException("Cannot find resource [" + name + "]");
        }

        Object table = rm.getConvertedResource(r.get(resourceType));
        return table;
    }

    private interface ProcessUTI {

        public void apply(Array2da baseItems, UtiGff item);
    }

    private FileFilter createModulesFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (!file.isFile() || file.isHidden()) {
                    return false;
                }
                String name = file.getName();
                if (!name.endsWith(".rim")) {
                    return false;
                }
                String baseName = FilenameUtils.getBaseName(name);
                if (baseName.endsWith("_s")) {
                    return false;
                }
                return true;
            }
        };
    }
}
