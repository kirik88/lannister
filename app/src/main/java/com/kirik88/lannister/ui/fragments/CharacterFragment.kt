package com.kirik88.lannister.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kirik88.lannister.databinding.FragmentCharacterBinding
import com.kirik88.lannister.vm.CharacterViewModel
import com.kirik88.lannister.vm.consume
import com.kirik88.lannister.vm.onError

class CharacterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharacterBinding.inflate(inflater, container, false)

        val vm: CharacterViewModel by viewModels()

        binding.viewModel = vm
        binding.lifecycleOwner = viewLifecycleOwner

        val args by navArgs<CharacterFragmentArgs>()
        vm.initCharacter(args.characterId)

        vm.openCharacter.observe(viewLifecycleOwner, Observer {
            it.consume(requireContext()) { characterId ->
                findNavController().navigate(
                    CharacterFragmentDirections
                        .actionCharacterFragmentSelf(characterId)
                )
            }
        })

        vm.fetchFather.observe(viewLifecycleOwner, Observer {
            it.consume(requireContext())
        })

        vm.fetchMother.observe(viewLifecycleOwner, Observer {
            it.consume(requireContext())
        })

        vm.fetchSpouse.observe(viewLifecycleOwner, Observer {
            it.consume(requireContext())
        })

        vm.refresher.observe(viewLifecycleOwner, Observer {
            it.onError {
                if (vm.isLoaded.value == false)
                    findNavController().navigateUp()
            }
            it.consume(requireContext())
        })

        return binding.root
    }
}