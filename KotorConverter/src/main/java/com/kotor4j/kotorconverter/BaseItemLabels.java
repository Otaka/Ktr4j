package com.kotor4j.kotorconverter;

import com.kotor4j.enums.WeaponType;
import com.kotor4j.enums.NotVisibleWearableType;
import java.util.HashMap;

/**
 * @author Dmitry
 */
public class BaseItemLabels {

    public static HashMap<String, WeaponBaseInfo> weaponLabelInfo = new HashMap<>();
    public static HashMap<String, WearableBaseInfo> wearableLabelInfo = new HashMap<>();

    //weapon
    static {
        addWeapon("Quarter_Staff", WeaponType.Staff);
        addWeapon("Stun_Baton", WeaponType.Dagger);
        addWeapon("Long_Sword", WeaponType.Blade);
        addWeapon("Vibro_Sword", WeaponType.Blade);
        addWeapon("Short_Sword", WeaponType.Blade);
        addWeapon("Vibro_Blade", WeaponType.Blade);
        addWeapon("Double_Bladed_Sword", WeaponType.Staff);
        addWeapon("Vibro_Double_Blade", WeaponType.Blade);
        addWeapon("Lightsaber", WeaponType.Lightsaber);
        addWeapon("Double_Bladed_Lightsaber", WeaponType.TwoHandedLightsaber);
        addWeapon("Short_Lightsaber", WeaponType.Lightsaber);
        addWeapon("Blaster_Pistol", WeaponType.Pistol);
        addWeapon("Heavy_Blaster", WeaponType.Pistol);
        addWeapon("Hold_Out_Blaster", WeaponType.Pistol);
        addWeapon("Ion_Blaster", WeaponType.Pistol);
        addWeapon("Disrupter_Pistol", WeaponType.Pistol);
        addWeapon("Sonic_Pistol", WeaponType.Pistol);
        addWeapon("Ion_Rifle", WeaponType.Riffle);
        addWeapon("Bowcaster", WeaponType.Riffle);
        addWeapon("Blaster_Carbine", WeaponType.Riffle);
        addWeapon("Disrupter_Rifle", WeaponType.Riffle);
        addWeapon("Sonic_Rifle", WeaponType.Riffle);
        addWeapon("Repeating_Blaster", WeaponType.Riffle);
        addWeapon("Heavy_Repeating_Blaster", WeaponType.Riffle);
        addWeapon("Blaster_Rifle", WeaponType.Riffle);
        addWeapon("Ghaffi_Stick", WeaponType.Staff);
        addWeapon("Wookie_Warblade", WeaponType.Blade);
        addWeapon("Gammorean_Battleaxe", WeaponType.Blade);
    }

    //wearable
    static {
        addWearable("Jedi_Robe", NotVisibleWearableType.Armor);
        addWearable("Jedi_Knight_Robe", NotVisibleWearableType.Armor);
        addWearable("Jedi_Master_Robe", NotVisibleWearableType.Armor);
        addWearable("Armor_Class_4", NotVisibleWearableType.Armor);
        addWearable("Armor_Class_5", NotVisibleWearableType.Armor);
        addWearable("Armor_Class_6", NotVisibleWearableType.Armor);
        addWearable("Armor_Class_7", NotVisibleWearableType.Armor);
        addWearable("Armor_Class_8", NotVisibleWearableType.Armor);
        addWearable("Armor_Class_9", NotVisibleWearableType.Armor);
        addWearable("Revan_Armor", NotVisibleWearableType.Armor);
        addWearable("Mask", NotVisibleWearableType.Mask);
        addWearable("Gauntlets", NotVisibleWearableType.Gloves);
        addWearable("Forearm_Bands", NotVisibleWearableType.Shield);
        addWearable("Belt", NotVisibleWearableType.Belt);
        addWearable("Implant_1", NotVisibleWearableType.Implant);
        addWearable("Implant_2", NotVisibleWearableType.Implant);
        addWearable("Implant_3", NotVisibleWearableType.Implant);
        addWearable("Basic_Clothing", NotVisibleWearableType.Armor);
        addWearable("Stealth_Unit", NotVisibleWearableType.Belt);
        addWearable("Disguise_Item", NotVisibleWearableType.Belt);
    }

    public static void addWeapon(String name, WeaponType weaponType) {
        weaponLabelInfo.put(name, new WeaponBaseInfo(name, weaponType));
    }

    public static void addWearable(String name, NotVisibleWearableType wearableType) {
        wearableLabelInfo.put(name, new WearableBaseInfo(name, wearableType));
    }

    public static class WeaponBaseInfo {

        private String name;
        private WeaponType weaponType;

        public WeaponBaseInfo(String name, WeaponType weaponType) {
            this.name = name;
            this.weaponType = weaponType;
        }

        public String getName() {
            return name;
        }

        public WeaponType getWeaponType() {
            return weaponType;
        }
    }

    public static class WearableBaseInfo {

        private String name;
        private NotVisibleWearableType wearableType;

        public WearableBaseInfo(String name, NotVisibleWearableType wearableType) {
            this.name = name;
            this.wearableType = wearableType;
        }

        public String getName() {
            return name;
        }

        public NotVisibleWearableType getWearableType() {
            return wearableType;
        }
    }
}
