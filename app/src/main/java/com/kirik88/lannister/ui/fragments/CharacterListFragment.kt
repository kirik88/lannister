package com.kirik88.lannister.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kirik88.lannister.databinding.FragmentCharacterListBinding
import com.kirik88.lannister.ui.adapters.CharacterAdapter
import com.kirik88.lannister.vm.CharacterListViewModel
import com.kirik88.lannister.vm.consume
import com.kirik88.lannister.vm.onError

class CharacterListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharacterListBinding.inflate(inflater, container, false)

        val vm: CharacterListViewModel by viewModels()

        binding.viewModel = vm
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = CharacterAdapter(CharacterAdapter.OnClickListener { character ->
            vm.onOpenCharacter(character.id)
        })
        binding.list.adapter = adapter
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // prevent loading at start
                if (adapter.itemCount == 0)
                    return

                // one loading at the same time
                if (vm.refresher.value?.inProgress == true || vm.refresher.value?.result != null)
                    return

                val lm = (recyclerView.layoutManager as GridLayoutManager)
                if (lm.findLastVisibleItemPosition() == adapter.itemCount - 1)
                    vm.onNextPage()
            }
        })

        vm.characters.observe(viewLifecycleOwner, Observer { characters ->
            adapter.submitList(characters)
        })

        vm.openCharacter.observe(viewLifecycleOwner, Observer {
            it.consume(requireContext()) { characterId ->
                findNavController().navigate(
                    CharacterListFragmentDirections
                        .actionCharacterListFragmentToCharacterFragment(characterId)
                )
            }
        })

        vm.refresher.observe(viewLifecycleOwner, Observer {
            it.onError {
                if (vm.isLoaded.value == false)
                    requireActivity().finish()
            }
            it.consume(requireContext())
        })

        return binding.root
    }
}