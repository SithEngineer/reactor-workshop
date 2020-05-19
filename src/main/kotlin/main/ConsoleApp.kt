package main

import reactive.UriDownloadCommand
import reactive.WebClient

fun main(args: Array<String>) {
    UriDownloadCommand(WebClient())
        .load(args)
        .subscribe { println(it) }
}