package mbolg.tasker.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_auth.*
import mbolg.tasker.R
import mbolg.tasker.data.api.auth.AuthListener
import mbolg.tasker.data.api.auth.Credentials
import mbolg.tasker.di.component.DaggerActivityComponent
import mbolg.tasker.di.module.ActivityModule
import mbolg.tasker.utils.AlertUtils
import mbolg.tasker.utils.NetworkUtils
import mbolg.tasker.utils.log
import mbolg.tasker.view.BaseActivity
import mbolg.tasker.view.tasklist.TaskListActivity
import javax.inject.Inject

class AuthActivity : BaseActivity(), AuthListener {

    @Inject
    lateinit var viewModel: AuthViewModel


    lateinit var currentFragment: Fragment

    private var signingIn = true

    private var showPassword = false

    private var actionButtonWidth = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(ActivityModule(this))
                .build()

        authComponent.inject(this)

        if (viewModel.isAuthed()) {
            goToTaskList()
            return
        }

        setContentView(R.layout.activity_auth)


        btnSignInRegisterSwitch.setOnClickListener { onSignInRegisterSwitchClick() }

        btnAction.setOnClickListener {
            if (!Validator.validateEmail(etEmail.text.toString())) {
                AlertUtils.showInvalidEmailAlert(this)
                return@setOnClickListener
            }
            if (!Validator.validatePassword(etPassword.text.toString())) {
                AlertUtils.showInvalidPasswordAlert(this)
                return@setOnClickListener
            }

            onNetworkOperationStart()
            if (signingIn)
                signIn()
            else signUp()
        }

        btnShowHidePassword.setOnClickListener {
            if (showPassword) {
                etPassword.transformationMethod = PasswordTransformationMethod()
                etPasswordConfirm.transformationMethod = PasswordTransformationMethod()
            } else {
                etPassword.transformationMethod = null
                etPasswordConfirm.transformationMethod = null
            }

            showPassword = !showPassword

        }

    }

    private fun onNetworkOperationStart() {
        btnAction.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE


        etEmail.isEnabled = false
        etPassword.isEnabled = false
        etPasswordConfirm.isEnabled = false
        btnSignInRegisterSwitch.visibility = View.INVISIBLE
    }

    private fun onNetworkOperationEnd() {
        btnAction.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE


        etEmail.isEnabled = true
        etPassword.isEnabled = true
        etPasswordConfirm.isEnabled = true
        btnSignInRegisterSwitch.visibility = View.VISIBLE
    }

    private fun goToTaskList() {
        val intent = Intent(this, TaskListActivity::class.java)
        startActivity(intent)
    }


    private fun signUp() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etPasswordConfirm.text.toString()

        if (password == confirmPassword) {

            val credentials = Credentials(email, password)
            viewModel.signUp(credentials, this)
        } else {
            AlertUtils.showPasswordConfirmCheckAlert(this)
        }

    }

    private fun signIn() {
        if (NetworkUtils.isOffline(this)) {
            AlertUtils.showSignInRequireNetworkAlert(this)
            return
        }
        val credentials = Credentials(etEmail.text.toString(), etPassword.text.toString())
        viewModel.signIn(credentials, this)
    }

    private fun onSignInRegisterSwitchClick() {
        signingIn = !signingIn

        if (signingIn) {
            btnAction.setText(R.string.signin)
            etPasswordConfirm.visibility = View.GONE
            btnSignInRegisterSwitch.setText(R.string.register)
        } else {
            btnAction.setText(R.string.register)
            etPasswordConfirm.visibility = View.VISIBLE
            btnSignInRegisterSwitch.setText(R.string.signin)
        }

    }


    override fun signInSuccess() {
        onNetworkOperationEnd()
        goToTaskList()
    }

    override fun signUpSuccess() {
        onNetworkOperationEnd()
        goToTaskList()
    }

    override fun signInFailed() {
        onNetworkOperationEnd()
        AlertUtils.showSignInError(this)
    }

    override fun signUpFailed() {
        onNetworkOperationEnd()
    }

}
