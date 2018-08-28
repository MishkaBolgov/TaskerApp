package mbolg.tasker.vocalizer

interface Vocalizer {
    fun vocalize(title: String, body: String, listener: ()->Unit)
}