package com.github.jiramas64.elevator;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Elevator extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN); // 1.足元のブロックを取得する
        if (block.getType() == Material.IRON_BLOCK) { // 2.鉄ブロックであった場合
            teleportToNextIronBlockJ(player, block, block.getY() + 1); // 再帰関数を呼び出す
        }
    }

    // 再帰関数の定義
    public void teleportToNextIronBlockJ(Player player, Block block, int y) {
        if (y < 319) { // 限界高度に到達するまで
            Block nextBlock = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
            if (nextBlock.getType() == Material.IRON_BLOCK) { // 鉄ブロックが見つかった場合
                if (nextBlock.getRelative(BlockFace.UP).getType() == Material.AIR && nextBlock.getRelative(BlockFace.UP, 2).getType() == Material.AIR) { // 3.鉄ブロックの上に空気ブロックが2つ以上あった場合
                    Location tpLocation = nextBlock.getLocation().add(0.5, 1, 0.5); // ブロックの中心にTPする
                    tpLocation.setPitch(player.getLocation().getPitch()); // プレイヤーの向きを保持する
                    tpLocation.setYaw(player.getLocation().getYaw());
                    player.teleport(tpLocation); // TPする
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f); // エンダーマンのテレポート音を再生する
                    return; // 再帰を終了する
                }
            }
            teleportToNextIronBlockJ(player, block, y + 1); // 再帰関数を呼び出す
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking()) { // スニークした場合
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN); // 1.足元のブロックを取得する
            if (block.getType() == Material.IRON_BLOCK) { // 2.鉄ブロックであった場合
                teleportToPreviousIronBlockS(player, block, block.getY() - 1); // 再帰関数を呼び出す
            }
        }
    }

    // 再帰関数の定義
    public void teleportToPreviousIronBlockS(Player player, Block block, int y) {
        if (y > -64) { // 限界低度に到達するまで
            Block previousBlock = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
            if (previousBlock.getType() == Material.IRON_BLOCK) { // 鉄ブロックが見つかった場合
                if (previousBlock.getRelative(BlockFace.UP).getType() == Material.AIR && previousBlock.getRelative(BlockFace.UP, 2).getType() == Material.AIR) { // 3.鉄ブロックの上に空気ブロックが2つ以上あった場合
                    Location tpLocation = previousBlock.getLocation().add(0.5, 1, 0.5); // ブロックの中心にTPする
                    tpLocation.setPitch(player.getLocation().getPitch()); // プレイヤーの向きを保持する
                    tpLocation.setYaw(player.getLocation().getYaw());
                    player.teleport(tpLocation); // TPする
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f); // エンダーマンのテレポート音を再生する
                    return; // 再帰を終了する
                }
            }
            teleportToPreviousIronBlockS(player, block, y - 1); // 再帰関数を呼び出す
        }
    }

}
