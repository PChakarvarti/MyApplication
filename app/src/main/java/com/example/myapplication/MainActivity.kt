package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.tools.javac.file.JavacFileManager
import com.sun.tools.javac.main.JavaCompiler
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.util.Context
import com.sun.tools.javac.util.List
import com.sun.tools.javac.util.Options
import java.io.File
import java.io.PrintWriter
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

    @Throws(Throwable::class)
    fun test(fm: JavacFileManager, f: JavaFileObject, vararg args: String?) {
        val context = Context()
        fm.setContext(context)
        val compilerMain = Main("javac", PrintWriter(System.err, true))
        compilerMain.setOptions(Options.instance(context))
        compilerMain.filenames = LinkedHashSet<File>()
        compilerMain.processArgs(args)
        val c = JavaCompiler.instance(context)
        c.compile(List.of(f))
        if (c.errorCount() != 0) throw java.lang.AssertionError("compilation failed")
        val msec = c.elapsed_msec
        if (msec < 0 || msec > 5 * 60 * 1000) throw java.lang.AssertionError("elapsed time is suspect: $msec")
    }
}