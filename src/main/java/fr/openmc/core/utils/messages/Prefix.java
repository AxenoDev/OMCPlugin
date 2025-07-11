package fr.openmc.core.utils.messages;

import lombok.Getter;

/**
 * Enum representing various prefixes for messages.
 * Each prefix is associated with a formatted string using custom colors and fonts.
 */
public enum Prefix {

    // Font: https://lingojam.com/MinecraftSmallFont
    // For gradient color: https://www.birdflop.com/resources/rgb/
    // Color format: MiniMessage

    OPENMC("<gradient:#BD45E6:#F99BEB>ᴏᴘᴇɴᴍᴄ</gradient>"),
    STAFF("<gradient:#AC3535:#8C052B>ѕᴛᴀꜰꜰ</gradient>"),
    CITY("<gradient:#026404:#2E8F38>ᴄɪᴛʏ</gradient>"),
    CONTEST("<gradient:#FFB800:#F0DF49>ᴄᴏɴᴛᴇѕᴛ</gradient>"),
    HOME("<gradient:#80EF80:#9aec9a>ʜᴏᴍᴇ</gradient>"),
    FRIEND("<gradient:#68E98B:#0EFF6D>ꜰʀɪᴇɴᴅ</gradient>"),
    MAYOR("<gradient:#FCD05C:#FBEF22>ᴍᴀʏ</gradient><gradient:#FBEF22:#FBEF22>ᴏʀ</gradient>"),
    QUEST("<gradient:#4E76E3:#1A51E7>ǫᴜᴇѕᴛ</gradient>"),
    BANK("<gradient:#084CFB:#ADB6FD>ʙᴀɴᴋ</gradient>"),
    ENTREPRISE("<gradient:#E2244F:#FE7474>ᴇɴᴛʀᴇᴘʀɪѕᴇ</gradient>"), // a changer si ça ne correspond pas
    SHOP("<gradient:#084CFB:#5AAFC4>ѕʜᴏᴘ</gradient>"),
    ADMINSHOP("<gradient:#EE2222:#F04949>ᴀᴅᴍɪɴꜱʜᴏᴘ</gradient>"),
    ACCOUTDETECTION("<gradient:#F45454:#545eb6>ᴀᴄᴄᴏᴜɴᴛ ᴅᴇᴛᴇᴄᴛɪᴏɴ</gradient>"),
    DEATH("<gradient:#FF0000:#FF7F7F>☠</gradient>"),
    SETTINGS("<gradient:#2C82E0:#67C8FF>ѕᴇᴛᴛɪɴɢѕ</gradient>"),
    MILLESTONE("<gradient:#A2D182:#B8E89D>ᴍɪʟʟᴇѕᴛᴏɴᴇ</gradient>")

    ;

    @Getter private final String prefix;
    Prefix(String prefix) {
        this.prefix = prefix;
    }
}