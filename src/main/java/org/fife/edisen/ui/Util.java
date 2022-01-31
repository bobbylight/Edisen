package org.fife.edisen.ui;

import org.fife.ui.OS;

/**
 * Obligatory utility methods.
 */
public final class Util {

    /**
     * Private constructor to prevent instantiation.
     */
    private Util() {
        // Do nothing (comment for Sonar)
    }

    public static String getDefaultAssemblerCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            // For some reason the combined cl65 doesn't seem to generate the same
            // .o files, and the generated .nes files don't run in Nestopia
            //return "cl65 -o ${rom} ${gameFile} -t nes";
            return "ca65 -o ${objfile} ${gameFile} -t nes";
//            return "ca65 -o ${objfile} ${gameFile} -t nes";
        }
        return "D:/cc65-snapshot-win32/bin/ca65 -o ${objfile} ${gameFile} -t nes";
    }

    public static String getDefaultLinkerCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            return "ld65 -o ${rom} ${objfile} -t nes";
//            return "ld65 -o ${rom} ${objfile} -C nesfile.ini";
        }
        return "D:/cc65-snapshot-win32/bin/ld65 -o ${rom} ${objfile} -t nes";
    }

    public static String getDefaultEmulatorCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            return "open -a Nestopia ${rom}";
        }
        return "D:/emulation/nes/emulators/nestopia/nestopia.exe ${rom}";
    }
}
