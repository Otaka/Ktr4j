package com.kotor4j.resourcemanager.chitinkey;

import com.kotor4j.resourcemanager.exceptions.ParsingException;
import static com.kotor4j.resourcemanager.chitinkey.ContentType.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry
 */
public enum ResourceType {

    TLK(-1, ContentType.TLK, "tlk"),
    BMP(1, BINARY, "bmp"),
    TGA(3, ContentType.TGA, "tga"),
    WAV(4, BINARY, "wav"),
    PLT(6, BINARY, "plt"),//not found in game resources
    //   INI(7, TEXT_INI, "ini"),
    TXT(10, TEXT, "txt"),//not found in game resources
    MDL(2002, MDL_MODEL, "mdl"),
    NSS(2009, TEXT, "nss"),
    NCS(2010, BYTECODE, "ncs"),
    ARE(2012, GFF, "are"),
    SET(2013, TEXT_INI, "set"),//not found in game resources
    IFO(2014, GFF, "ifo"),
    BIC(2015, GFF, "bic"),
    WOK(2016, WALKMESH, "wok"),
    _2DA(2017, ContentType._2DA, "2da"),
    TXI(2022, TEXT, "txi"),
    GIT(2023, GFF, "git"),
    UTI(2025, GFF, "uti"),
    UTC(2027, GFF, "utc"),
    DLG(2029, GFF, "dlg"),
    ITP(2030, GFF, "itp"),
    UTT(2032, GFF, "utt"),
    DDS(2033, BINARY, "dds"),//not found in game resources
    UTS(2035, GFF, "uts"),
    LTR(2036, BINARY, "ltr"),
    //_GFF(2037, GFF),
    FAC(2038, GFF, "fac"),
    UTE(2040, GFF, "ute"),
    UTD(2042, GFF, "utd"),
    UTP(2044, GFF, "utp"),
    //DFT(2045, TEXT_INI),
    //GIC(2046, GFF),
    GUI(2047, GFF, "gui"),
    UTM(2051, GFF, "utm"),
    DWK(2052, WALKMESH, "dwk"),/////
    PWK(2053, WALKMESH, "pwk"),///////
    JRL(2056, GFF, "jrl"),
    UTW(2058, GFF, "utw"),
    SSF(2060, ContentType.SSF, "ssf"),
    NDB(2061, BINARY, "ndb"),
    //PTM(2065, GFF),
    //PTT(2066, GFF),
    LYT(3000, TEXT, "lyt"),
    MDX(3008, MDX_MODEL, "mdx"),
    BTC(2026, GFF, "btc"),
    BTI(2024, GFF, "bti"),
    VIS(3001, ContentType.VIS, "vis"),
    PTH(3003, GFF, "pth"),
    TPC(3007, TPC_TEXTURE, "tpc");

    private final int id;
    private final ContentType contentType;
    private static final Map<Integer, ResourceType> resourceTypeMap = new HashMap<>();
    private String extension;
    private static final Map<String, ResourceType> extensionToTypeMap = new HashMap<>();

    static {
        for (ResourceType rt : values()) {
            resourceTypeMap.put(rt.getId(), rt);
        }
        for (ResourceType rt : values()) {
            extensionToTypeMap.put(rt.getExtension(), rt);
        }
    }

    public static ResourceType getByType(int id) {
        ResourceType type = resourceTypeMap.get(id);
        if (type == null) {
            throw new ParsingException("Cannot determine ResourceType by this [" + id + "] id");
        }
        return type;
    }

    private ResourceType(int id, ContentType contentType, String extension) {
        this.id = id;
        this.contentType = contentType;
        this.extension = extension;
    }

    public int getId() {
        return id;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }

    public static ResourceType getResourceTypeByExtension(String extension) {
        extension = extension.toLowerCase();
        return extensionToTypeMap.get(extension);
    }

}
