package xyz.jpenilla.squaremap.common.command.commands;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.extra.confirmation.CommandConfirmationManager;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import com.google.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.server.level.ServerLevel;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.common.WorldManager;
import xyz.jpenilla.squaremap.common.command.Commander;
import xyz.jpenilla.squaremap.common.command.Commands;
import xyz.jpenilla.squaremap.common.command.SquaremapCommand;
import xyz.jpenilla.squaremap.common.command.argument.LevelArgument;
import xyz.jpenilla.squaremap.common.config.Messages;
import xyz.jpenilla.squaremap.common.data.DirectoryProvider;
import xyz.jpenilla.squaremap.common.data.MapWorldInternal;
import xyz.jpenilla.squaremap.common.util.Components;
import xyz.jpenilla.squaremap.common.util.FileUtil;

@DefaultQualifier(NonNull.class)
public final class ResetMapCommand extends SquaremapCommand {
    private final DirectoryProvider directoryProvider;
    private final WorldManager worldManager;

    @Inject
    private ResetMapCommand(
        final Commands commands,
        final DirectoryProvider directoryProvider,
        final WorldManager worldManager
    ) {
        super(commands);
        this.directoryProvider = directoryProvider;
        this.worldManager = worldManager;
    }

    @Override
    public void register() {
        this.commands.registerSubcommand(builder ->
            builder.literal("resetmap")
                .argument(LevelArgument.of("world"))
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Messages.RESETMAP_COMMAND_DESCRIPTION.asComponent())
                .meta(CommandConfirmationManager.META_CONFIRMATION_REQUIRED, true)
                .permission("squaremap.command.resetmap")
                .handler(this::executeResetMap));
    }

    private void executeResetMap(final CommandContext<Commander> context) {
        final Commander sender = context.getSender();
        final ServerLevel world = context.get("world");
        final Path worldTilesDir = this.directoryProvider.getAndCreateTilesDirectory(world);
        try {
            FileUtil.deleteContentsRecursively(worldTilesDir);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not reset map for level '" + world.dimension().location() + "'", ex);
        }
        this.worldManager.getWorldIfEnabled(world).ifPresent(MapWorldInternal::didReset);
        sender.sendMessage(Messages.SUCCESSFULLY_RESET_MAP.withPlaceholders(Components.worldPlaceholder(world)));
    }
}
