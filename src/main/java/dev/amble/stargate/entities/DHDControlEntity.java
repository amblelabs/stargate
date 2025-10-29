package dev.amble.stargate.entities;

import dev.amble.stargate.api.address.Glyph;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.init.StargateEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DHDControlEntity extends Entity {

    private BlockPos dhdPos;

    private char symbol;
    private EntityDimensions dimensions = StargateEntities.DHD_CONTROL_TYPE.getDimensions();

    public DHDControlEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
    }

    public DHDControlEntity(World world, char symbol, EntityDimensions dimensions, BlockPos dhdPos) {
        this(StargateEntities.DHD_CONTROL_TYPE, world);

        this.symbol = symbol;
        this.dimensions = dimensions;

        this.dhdPos = dhdPos;
    }

    @Override
    protected void initDataTracker() { }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public void onRemoved() {
        if (this.dhdPos != null && this.getWorld().getBlockEntity(this.dhdPos) instanceof DHDBlockEntity dhd)
            dhd.markNeedsControl();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        if (dhdPos != null)
            nbt.put("dhd", NbtHelper.fromBlockPos(this.dhdPos));

        nbt.putFloat("width", dimensions.width);
        nbt.putFloat("height", dimensions.height);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        NbtCompound dhd = nbt.getCompound("dhd");

        if (dhd != null)
            this.dhdPos = NbtHelper.toBlockPos(dhd);

        if (nbt.contains("width") && nbt.contains("height")) {
            float width = nbt.getFloat("width");
            float height = nbt.getFloat("height");

            this.dimensions = EntityDimensions.fixed(width, height);
            this.calculateDimensions();
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        this.run(player, false);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!(source.getAttacker() instanceof PlayerEntity player)) return false;

        this.run(player, true);
        return true;
    }

    @Override
    public Text getName() {
        return Glyph.asText(this.symbol);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return dimensions;
    }

    @Override
    public void tick() {
        if (this.getWorld().isClient())
            return;

        if (this.dhdPos == null)
            this.discard();
    }

    private void run(PlayerEntity player, boolean leftClick) {
        if (this.getWorld().isClient())
            return;

        if (this.getWorld().getBlockEntity(this.dhdPos) instanceof DHDBlockEntity dhd) {
            dhd.onUseControl(player, this.getWorld(), this, leftClick);
        } else {
            this.discard();
        }
    }

    public char getSymbol() {
        return symbol;
    }
}
