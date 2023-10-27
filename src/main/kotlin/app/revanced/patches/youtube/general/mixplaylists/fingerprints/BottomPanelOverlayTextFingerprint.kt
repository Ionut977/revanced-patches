package app.revanced.patches.youtube.general.mixplaylists.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import app.revanced.patches.youtube.utils.resourceid.SharedResourceIdPatch.BottomPanelOverlayText
import app.revanced.util.bytecode.isWideLiteralExists
import com.android.tools.smali.dexlib2.Opcode

object BottomPanelOverlayTextFingerprint : MethodFingerprint(
    opcodes = listOf(
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.IPUT_OBJECT
    ),
    customFingerprint = { methodDef, _ -> methodDef.isWideLiteralExists(BottomPanelOverlayText) }
)