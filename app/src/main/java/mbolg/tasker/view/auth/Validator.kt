package mbolg.tasker.view.auth

class Validator {

    companion object {
        fun validateEmail(email: String): Boolean {
            if (email.length < 6)
                return false

            return true
        }

        fun validatePassword(password: String): Boolean {
            if (password.length < 6)
                return false

            return true
        }
    }
}