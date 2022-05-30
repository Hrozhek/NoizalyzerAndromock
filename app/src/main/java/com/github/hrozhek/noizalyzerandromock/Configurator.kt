package com.github.hrozhek.noizalyzerandromock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.hrozhek.noizalyzerandromock.databinding.ConfiguratorBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Configurator : Fragment() {

    private var _binding: ConfiguratorBinding? = null
//
//    private lateinit var server: String
//    private var port: Int = 8080
//    private lateinit var endpoint: String


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = ConfiguratorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val server = binding.editTextTextPersonName.text.toString()
            val port = binding.editTextTextPersonName2.text.toString().toInt()
            val endpoint = binding.editTextTextPersonName3.text.toString()
            val client = ConnectionClient(server, port, endpoint);
            Thread(client::initConnection).start()

            findNavController().navigate(R.id.action_Config_to_MainCycle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}