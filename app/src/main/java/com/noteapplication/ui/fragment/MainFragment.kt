package com.noteapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.noteapplication.R
import com.noteapplication.adapter.NoteAdapter
import com.noteapplication.api.NetworkResult
import com.noteapplication.databinding.FragmentMainBinding
import com.noteapplication.models.Note
import com.noteapplication.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()

    lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NoteAdapter(::onNoteClicked)

        binding.noteList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
        noteViewModel.getNotes()

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }

        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.noteListMutableData.observe(viewLifecycleOwner, Observer {
            binding.pbProgressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adapter.submitList(it.data!!)
                }
                is NetworkResult.Error -> {
                    val note1 = Note("123", "title1", "desc1 jkldsflskdjfldjfl", "9876")
                    val note2 =
                        Note("456", "title2", "desc2 djsflkdsjfljslfjldsjflkdsjflsdjfjsdl", "9876")
                    val note3 = Note("789", "title3", "desc3 djflsj", "9876")
                    var noteList = listOf<Note>(note1, note2, note3)
                    adapter.submitList(noteList)
                    binding.textView.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.pbProgressBar.isVisible = true
                }
            }
        })
    }

    private fun onNoteClicked(note: Note) {
        var bundle = Bundle()
        bundle.putString("note", Gson().toJson(note))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}