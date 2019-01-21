package solutions.mckee.crux.utils

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)
