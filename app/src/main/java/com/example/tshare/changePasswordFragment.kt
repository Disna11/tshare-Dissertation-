package com.example.tshare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class changePasswordFragment : Fragment() {
    private lateinit var password:EditText
    private lateinit var newpassword:EditText
    private lateinit var confirmpassword:EditText
    private lateinit var changepasswordButton:Button
    var preferenceHelper: preferenceHelper? =null
    private lateinit var auth: FirebaseAuth
    var user: FirebaseUser?=null
    var savedPassword:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_change_password, container, false)
        password=view.findViewById(R.id.oldPassword)
        newpassword=view.findViewById(R.id.newPassword)
        confirmpassword=view.findViewById(R.id.confirmPassword)
        changepasswordButton=view.findViewById(R.id.changePasswordButton)
        preferenceHelper = preferenceHelper(requireContext())
        auth = FirebaseAuth.getInstance()
        user=auth.currentUser
        savedPassword= preferenceHelper!!.getString("userpassword", "")


        val closeButton = view.findViewById<FloatingActionButton>(R.id.close_button)
        closeButton?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        changepasswordButton?.setOnClickListener {
            if(password.text.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Your Password field is empty!",Toast.LENGTH_SHORT).show()
            }else if(newpassword.text.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Your new password field is empty",Toast.LENGTH_SHORT).show()
            }else if(confirmpassword.text.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Your confirm password field is empty",Toast.LENGTH_SHORT).show()
            }
            if(password.text.toString()!=savedPassword){
                Toast.makeText(requireContext(),"Enter the correct password",Toast.LENGTH_SHORT).show()
            }
            else{
                if(newpassword.text.toString()!=confirmpassword.text.toString()){
                    Toast.makeText(requireContext(),"Password Mismatch",Toast.LENGTH_SHORT).show()
                }else {
                    user?.updatePassword(newpassword.text.toString())
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(), "Password Updated successfully!", Toast.LENGTH_SHORT).show()
                                requireActivity().supportFragmentManager.popBackStack()
                            }
                        }
                        ?.addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Password Updation failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }

            }

        }

        return view
    }


}