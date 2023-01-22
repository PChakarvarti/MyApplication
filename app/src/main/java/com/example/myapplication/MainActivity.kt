package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.tools.javac.file.JavacFileManager
import com.sun.tools.javac.main.JavaCompiler
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.util.Context
import com.sun.tools.javac.util.List
import java.io.File
import javax.tools.JavaFileObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val compiler = JavaCompiler.instance(Context())
        compiler.keepComments = true

        val testSrc = System.getProperty("test.src")
        val fm = JavacFileManager(Context(), false, null)
        val f: JavaFileObject = fm.getJavaFileObject(testSrc + File.separatorChar + "T4910483.java")

        val cu: JCTree.JCCompilationUnit = compiler.parse(f)
        val classDef: JCTree = cu.getTypeDecls().head
        val commentText: String = cu.docComments.getCommentText(classDef)

        val expected = "Test comment abc*\\\\def" // 4 '\' escapes to 2 in a string literal

        if (expected != commentText) {
            throw AssertionError("Incorrect comment text: [$commentText], expected [$expected]")
        }
    }
}