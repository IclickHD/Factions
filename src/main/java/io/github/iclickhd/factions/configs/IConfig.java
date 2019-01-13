package io.github.iclickhd.factions.configs;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public interface IConfig {
    void setup();

    void load();

    void save();

    void populate();

    CommentedConfigurationNode get();
}
