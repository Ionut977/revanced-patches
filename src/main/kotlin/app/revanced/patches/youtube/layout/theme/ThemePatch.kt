package app.revanced.patches.youtube.layout.theme

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.patch.options.PatchOption.PatchExtensions.stringPatchOption
import app.revanced.patches.youtube.layout.theme.GeneralThemePatch.isMonetPatchIncluded
import app.revanced.patches.youtube.utils.settings.SettingsPatch
import app.revanced.util.resources.ResourceHelper.updatePatchStatusTheme
import org.w3c.dom.Element

@Patch(
    name = "Theme",
    description = "Change the app's theme to the values specified in options.json.",
    dependencies = [
        GeneralThemePatch::class,
        SettingsPatch::class
    ],
    compatiblePackages = [
        CompatiblePackage(
            "com.google.android.youtube",
            [
                "18.24.37",
                "18.25.40",
                "18.27.36",
                "18.29.38",
                "18.30.37",
                "18.31.40",
                "18.32.39",
                "18.33.40",
                "18.34.38",
                "18.35.36",
                "18.36.39",
                "18.37.36",
                "18.38.44",
                "18.39.41",
                "18.40.34"
            ]
        )
    ]
)
@Suppress("unused")
object ThemePatch : ResourcePatch() {
    private const val AMOLED_BLACK_COLOR = "@android:color/black"
    override fun execute(context: ResourceContext) {

        arrayOf("values", "values-v31").forEach { context.setTheme(it) }

        val currentTheme = if (isMonetPatchIncluded) "mix" else "amoled"

        context.updatePatchStatusTheme(currentTheme)

    }

    private fun ResourceContext.setTheme(valuesPath: String) {
        val darkThemeColor = darkThemeBackgroundColor
            ?: throw PatchException("Invalid color.")

        this.xmlEditor["res/$valuesPath/colors.xml"].use { editor ->
            val resourcesNode = editor.file.getElementsByTagName("resources").item(0) as Element

            for (i in 0 until resourcesNode.childNodes.length) {
                val node = resourcesNode.childNodes.item(i) as? Element ?: continue

                node.textContent = when (node.getAttribute("name")) {
                    "yt_black0", "yt_black1", "yt_black1_opacity95", "yt_black1_opacity98", "yt_black2", "yt_black3",
                    "yt_black4", "yt_status_bar_background_dark", "material_grey_850" -> darkThemeColor

                    else -> continue
                }
            }
        }
    }

    internal var darkThemeBackgroundColor by stringPatchOption(
        key = "DarkThemeBackgroundColor",
        default = AMOLED_BLACK_COLOR,
        values = mapOf(
            "Amoled black" to AMOLED_BLACK_COLOR,
            "Catppuccin (Mocha)" to "#FF181825",
            "Dark pink" to "#FF290025",
            "Dark blue" to "#FF001029",
            "Dark green" to "#FF002905",
            "Dark yellow" to "#FF282900",
            "Dark orange" to "#FF291800",
            "Dark red" to "#FF290000"
        ),
        title = "Dark theme background color",
        description = "Can be a hex color (#AARRGGBB) or a color resource reference.",
    )
}
