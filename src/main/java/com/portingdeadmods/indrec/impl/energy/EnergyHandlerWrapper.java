package com.portingdeadmods.indrec.impl.energy;

import com.portingdeadmods.indrec.api.energy.EnergyHandler;
import com.portingdeadmods.indrec.api.energy.EnergyTier;

public class EnergyHandlerWrapper {
    protected final EnergyHandler handler;

    public EnergyHandlerWrapper(EnergyHandler handler) {
        this.handler = handler;
    }

    public static final class NoDrain extends EnergyHandlerWrapper implements EnergyHandler.NoDrain {
        public NoDrain(EnergyHandler handler) {
            super(handler);
        }

        @Override
        public EnergyTier getEnergyTier() {
            return this.handler.getEnergyTier();
        }

        @Override
        public int getEnergyStored() {
            return this.handler.getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            this.handler.setEnergyStored(value);
        }

        @Override
        public int getEnergyCapacity() {
            return this.handler.getEnergyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            this.handler.setEnergyCapacity(value);
        }
    }

    public static final class NoFill extends EnergyHandlerWrapper implements EnergyHandler.NoFill {
        public NoFill(EnergyHandler handler) {
            super(handler);
        }

        @Override
        public EnergyTier getEnergyTier() {
            return this.handler.getEnergyTier();
        }

        @Override
        public int getEnergyStored() {
            return this.handler.getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            this.handler.setEnergyStored(value);
        }

        @Override
        public int getEnergyCapacity() {
            return this.handler.getEnergyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            this.handler.setEnergyCapacity(value);
        }
    }

    /**
     * One side of a transformer, which all share a single buffer. The transformer decides whether energy may
     * enter through this side, and sends the energy out by itself, so nothing can be drained from here.
     */
    public static final class TransformerSide extends EnergyHandlerWrapper implements EnergyHandler.NoDrain {
        private final EnergyTier energyTier;
        private final InsertListener insertListener;

        public TransformerSide(EnergyHandler buffer, EnergyTier energyTier, InsertListener insertListener) {
            super(buffer);
            this.energyTier = energyTier;
            this.insertListener = insertListener;
        }

        @Override
        public EnergyTier getEnergyTier() {
            return this.energyTier;
        }

        // Unlike other handlers, transformers have no tolerance for energy of a lower tier
        @Override
        public boolean isValidTier(EnergyTier energyTier) {
            return energyTier.compareTo(this.energyTier) >= 0;
        }

        @Override
        public int fillEnergy(int value, boolean simulate) {
            if (value <= 0 || !this.insertListener.onInsert(simulate)) {
                return 0;
            }
            return EnergyHandler.NoDrain.super.fillEnergy(value, simulate);
        }

        @Override
        public int forceDrainEnergy(int value, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return this.handler.getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            this.handler.setEnergyStored(value);
        }

        @Override
        public int getEnergyCapacity() {
            return this.handler.getEnergyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            this.handler.setEnergyCapacity(value);
        }

        @FunctionalInterface
        public interface InsertListener {
            /**
             * @return whether the energy may enter
             */
            boolean onInsert(boolean simulate);
        }
    }

}