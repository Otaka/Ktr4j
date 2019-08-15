package com.ncsdcmp.analyzers;

/**
 * @author sad
 */
public abstract class AbstractAnalyzer {

    private AnalyzeContext context;

    public abstract void analyze();

    public void setContext(AnalyzeContext context) {
        this.context = context;
    }

    public AnalyzeContext getContext() {
        return context;
    }

    protected int getInstructionIndexByAddress(int address) {
        for (int i = 0; i < context.getLines().size(); i++) {
            if (context.getLines().get(i).getAddress() == address) {
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot find line started from address [" + address + "]");
    }

    protected int parseSignedHexInt(String hex) {
        long value = Long.parseLong(hex, 16);
        int result = (int) (value & 0xFFFFFFFF);
        return result;
    }
}
