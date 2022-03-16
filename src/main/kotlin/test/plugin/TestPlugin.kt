package test.plugin

import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info("Enabling test plugin...")
    }

    override fun onDisable() {
        logger.info("Disabling test plugin...")
    }
}