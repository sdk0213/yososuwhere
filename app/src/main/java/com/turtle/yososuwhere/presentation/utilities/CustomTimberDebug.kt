package com.turtle.yososuwhere.presentation.utilities

import timber.log.Timber

class CustomTimberDebug : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? { 
        return "${element.fileName}:${element.lineNumber}#${element.methodName}" 
    } 
}