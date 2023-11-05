package com.pena.ismael.socialmediapager.core.services.downloadmanager

interface Downloader {
    fun downloadImage(url: String, fileName: String): Long?
}