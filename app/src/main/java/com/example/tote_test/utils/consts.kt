package com.example.tote_test.utils

import com.example.tote_test.firebase.FirebaseRepository
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

lateinit var APP_ACTIVITY: MainActivity

const val LOG_TAG = "logTote"
const val EMPTY = "empty"
const val YEAR_START = 2021
const val MIN_PASSWORD_LENGTH = 6

lateinit var REPOSITORY: FirebaseRepository
lateinit var AUTH: FirebaseAuth
lateinit var REF_DB_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference

const val FOLDER_PROFILE_PHOTO = "profile_photo"

const val CHILD_ID = "id"

const val NODE_GAMBLERS = "gamblers"

var GAMBLER = GamblerModel()
lateinit var CURRENT_ID: String
lateinit var EMAIL: String
lateinit var PASSWORD: String
var IS_ADMIN: Boolean = false

//Gambler model fields
const val GAMBLER_NICKNAME = "nickname"
const val GAMBLER_EMAIL = "email"
const val GAMBLER_FAMILY = "family"
const val GAMBLER_NAME = "name"
const val GAMBLER_GENDER = "gender"
const val GAMBLER_PHOTO_URL = "photoUrl"
const val GAMBLER_STAKE = "stake"
const val GAMBLER_POINTS = "points"
const val GAMBLER_PREV_PLACE = "placePrev"
const val GAMBLER_PLACE = "place"
const val GAMBLER_IS_ADMIN = "isAdmin"


