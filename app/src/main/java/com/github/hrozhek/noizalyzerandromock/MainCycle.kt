package com.github.hrozhek.noizalyzerandromock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.hrozhek.noizalyzerandromock.databinding.MainCycleBinding
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MainCycle : Fragment() {

    private val context: AppContext = AppContext.getAppCon()
    private var _binding: MainCycleBinding? = null
    private var executor: ScheduledExecutorService? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MainCycleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        startMainCycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        executor?.shutdownNow()
        _binding = null
    }

    fun startMainCycle() {
        val timeout = context.writeTimeout
        executor = Executors.newSingleThreadScheduledExecutor()
        val worker = MainCycleWorker(this.getContext())//TODO stop
        Thread { ->
            worker.init()
            executor?.scheduleAtFixedRate(worker, 0, timeout.time, timeout.timeUnit)
        }.start()
    }
}