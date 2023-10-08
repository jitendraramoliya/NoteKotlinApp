package com.noteapplication.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.noteapplication.api.NetworkResult
import com.noteapplication.databinding.FragmentNoteBinding
import com.noteapplication.models.Note
import com.noteapplication.models.NoteRequest
import com.noteapplication.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    lateinit var note: Note
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()

        binding.btnSubmit.setOnClickListener {
            val validateInput = validateInput()
            if (validateInput) {
                if (note != null) {
                    noteViewModel.updateNote("12345", getNoteRequest())
                } else {
                    noteViewModel.createNote(getNoteRequest())
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            if (::note.isInitialized) {
                noteViewModel.deleteNote("12345")
            }
        }

        bindObservable()
    }

    private fun bindObservable() {
        noteViewModel.noteListData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getNoteRequest(): NoteRequest {
        val title = binding.txtTitle.text.toString()
        val desc = binding.txtDescription.text.toString()
        return NoteRequest(title, desc)
    }

    private fun validateInput(): Boolean {
        val (title, desc) = getNoteRequest()

        return if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, "Title should not be empty", Toast.LENGTH_SHORT).show()
            false
        } else if (TextUtils.isEmpty(desc)) {
            Toast.makeText(context, "Description should not be empty", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }

    }

    private fun setInitialData() {
        val gsonNote = arguments?.getString("note")
        if (gsonNote != null) {
            note = Gson().fromJson(gsonNote, Note::class.java)
            note.let {
                binding.txtTitle.setText(note.title)
                binding.txtDescription.setText(note.description)
            }
        } else {
            binding.addEditText.text = "Add Note"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}