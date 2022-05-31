package com.github.hrozhek.noizalyzerandromock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.hrozhek.noizalyzerandromock.databinding.ConfiguratorBinding
import java.util.concurrent.TimeUnit

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
            readValuesToContext()
            findNavController().navigate(R.id.action_Config_to_MainCycle)
        }
    }

    private fun readValuesToContext() {
        val ctx = AppContext.getAppCon()
        ctx.server = wrapTryCatch({ -> binding.editTextTextPersonName.text.toString()}, "localhost")
        ctx.port = wrapTryCatch({ -> binding.editTextTextPersonName2.text.toString().toInt()}, 8080)
        ctx.endpoint = wrapTryCatch({ -> binding.editTextTextPersonName3.text.toString()}, "noizalyzer")

        ctx.connectionClient = ConnectionClient(ctx.server, ctx.port, ctx.endpoint);

        ctx.writeTimeout = wrapTryCatch({ ->
            Timeout(binding.editTextTextPersonName4.text.toString().toLong(),
                TimeUnit.valueOf(binding.editTextTextPersonName5.text.toString().uppercase()))
        }, Timeout(1, TimeUnit.MINUTES))

        ctx.readTimeout = wrapTryCatch({ -> Timeout(
            binding.editTextTextPersonName6.text.toString().toLong(),
                TimeUnit.valueOf(binding.editTextTextPersonName7.text.toString().uppercase()))
        }, Timeout(3, TimeUnit.SECONDS))
    }

    private fun <T> wrapTryCatch(func: Function0<T>, defaultValue: T): T {
        try {
            return func.invoke()
        } catch (e: Exception) {
            println(e)//TODO
            return defaultValue
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}