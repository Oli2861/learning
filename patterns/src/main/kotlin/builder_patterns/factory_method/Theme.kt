package builder_patterns.factory_method

/**
 * Abstract product class.
 */
abstract class Theme(
    protected val preferenceColor: String
) {

    abstract fun getButton(): Button
}

/**
 * Specific product class.
 */
class DarkTheme(
    preferenceColor: String
) : Theme(preferenceColor) {

    override fun getButton(): Button{
        return Button("dark-grey", preferenceColor)
    }

}

/**
 * Specific product class.
 */
class LightTheme(
    preferenceColor: String
) : Theme(preferenceColor) {

    override fun getButton(): Button{
        return Button("white", preferenceColor)
    }

}

class Button(backgroundColor: String, textColor: String)