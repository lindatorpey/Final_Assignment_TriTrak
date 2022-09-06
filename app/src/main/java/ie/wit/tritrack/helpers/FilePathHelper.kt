package ie.wit.tritrack.helpers

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

object FilePathHelper {
    fun getPathFromUri(contextDG: Context, uriDG: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(contextDG, uriDG)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uriDG)) {
                val docId = DocumentsContract.getDocumentId(uriDG)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                return if ("primary".equals(type, ignoreCase = true)) {
                    if (split.size > 1) {
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1] + "/"
                    } else {
                        Environment.getExternalStorageDirectory().toString() + "/"
                    }
                    // This is for checking SD Card
                } else {
                    "storage" + "/" + docId.replace(":", "/")
                } // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uriDG)) {
                val id = DocumentsContract.getDocumentId(uriDG)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataDGColumn(contextDG, contentUri, null, null)
            } else if (isMediaDocument(uriDG)) {
                val docId = DocumentsContract.getDocumentId(uriDG)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataDGColumn(contextDG, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uriDG.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uriDG)) uriDG.lastPathSegment else getDataDGColumn(
                contextDG,
                uriDG,
                null,
                null
            )
        } else if ("file".equals(uriDG.scheme, ignoreCase = true)) {
            return uriDG.path
        }
        return null
    }

    fun getDataDGColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}