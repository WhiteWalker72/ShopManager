package net.whitewalker.shopmanager.persistence;

import net.rayze.core.services.MongoDB;
import org.bukkit.configuration.file.FileConfiguration;

class MlabDatabase extends MongoDB {

    MlabDatabase(FileConfiguration config) {
        super(config.getString("mongo.host"), config.getString("mongo.user"), config.getString("mongo.database"), config.getString("mongo.password"));
    }

}
