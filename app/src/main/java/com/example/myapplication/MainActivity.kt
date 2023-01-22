package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.tools.javac.main.JavaCompiler
import com.sun.tools.javac.util.List
import javax.tools.JavaFileObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var compiler: JavaCompiler
        var obj:JavaFileObject = JavaFileObject()
        compiler.compile(listOf(obj) as List<JavaFileObject>?)
    }
}