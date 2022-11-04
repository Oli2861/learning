package builder_patterns.factory_method

/**
 * Abstract creator class.
 */
abstract class Window(
    protected val windowWidth: Int,
    protected val windowHeight: Int,
) {

    fun drawWindow() {
        val chosenColor = getUserPreferenceColor()
        val theme = createTheme(chosenColor)

        val button = theme.getButton()
        //...
    }

    private fun getUserPreferenceColor(): String{
        //..
        return "red"
    }

    abstract fun createTheme(preferenceColor: String): Theme

}

/**
 * Specific creator class containing a factory method to create a DarkTheme.
 */
class NighttimeWindow(
    windowWidth: Int,
    windowHeight: Int
) : Window(windowWidth, windowHeight) {

    override fun createTheme(preferenceColor: String): Theme {
        return DarkTheme(preferenceColor)
    }

}

/**
 * Specific creator class containing a factory method to create a LightTheme.
 */
class DaytimeWindow(
    windowWidth: Int,
    windowHeight: Int
) : Window(windowWidth, windowHeight) {

    override fun createTheme(preferenceColor: String): Theme {
        return LightTheme(preferenceColor)
    }

}