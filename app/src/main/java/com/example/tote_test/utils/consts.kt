package com.example.tote_test.utils

import com.example.tote_test.firebase.FirebaseRepository
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.TeamModel
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
const val NODE_GAMES = "games"
const val NODE_TEAMS = "teams"

var GAMBLER = GamblerModel()
lateinit var CURRENT_ID: String
lateinit var EMAIL: String
lateinit var PASSWORD: String
var IS_ADMIN: Boolean = false

//Gambler model fields
//const val GAMBLER_NICKNAME = "nickname"
//const val GAMBLER_EMAIL = "email"
//const val GAMBLER_FAMILY = "family"
//const val GAMBLER_NAME = "name"
//const val GAMBLER_GENDER = "gender"
const val GAMBLER_PHOTO_URL = "photoUrl"
//const val GAMBLER_STAKE = "stake"
//const val GAMBLER_POINTS = "points"
//const val GAMBLER_PREV_PLACE = "placePrev"
//const val GAMBLER_PLACE = "place"
//const val GAMBLER_IS_ADMIN = "admin"
//const val GAMBLER_IS_ACTIVE = "active"

val TEAMS = arrayListOf<TeamModel>(
    TeamModel(
        team = "Катар",
        group = "A",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fqat.png?alt=media&token=c0b7992a-540e-4e2e-84bf-ea6db9f27c83",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Эквадор",
        group = "A",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fecu.png?alt=media&token=e1f84a6d-2563-4cc9-804d-3ee6521a0ca0",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "Сенегал",
        group = "A",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fsen.png?alt=media&token=9cd3284c-4e86-4191-add3-0f663721d3d2",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Нидерланды",
        group = "A",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fned.png?alt=media&token=61a54f6a-8dae-4e38-8849-13dc70037640",
        sequenceNumber = 4
    ),
    TeamModel(
        team = "Англия",
        group = "B",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Feng.png?alt=media&token=05abb084-0cfe-426b-ab37-33b95e0ba8ee",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Иран",
        group = "B",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Firn.png?alt=media&token=9aa20998-6091-41fa-9c70-6b6be16c3334",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "США",
        group = "B",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fusa.png?alt=media&token=8eae0720-d38b-49e0-b106-392de6a6de69",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Уэльс",
        group = "B",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fwal.png?alt=media&token=23b149dd-a21b-45e9-9826-9b723cc6af86",
        sequenceNumber = 4
    ),
    TeamModel(
        team = "Аргентина",
        group = "C",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Farg.png?alt=media&token=846e536a-8690-443b-9239-834966c5186b",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Саудовская Аравия",
        group = "C",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fksa.png?alt=media&token=1ccb82aa-85bb-4002-8473-c39cd57a3aa3",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "Мексика",
        group = "C",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fmex.png?alt=media&token=5adc7e61-9ce0-4a75-9bf7-2da4f765cb2f",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Польша",
        group = "C",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fpol.png?alt=media&token=8cc048b1-d2c4-4059-a218-cbd7ee96e5a1",
        sequenceNumber = 4
    ),
    TeamModel(
        team = "Франция",
        group = "D",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Ffra.png?alt=media&token=0ec47061-3dd4-4cad-bff7-c6f15a5107cc",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Дания",
        group = "D",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fden.png?alt=media&token=5f28f77d-038d-49ef-86f3-6bcba389ee2e",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "Тунис",
        group = "D",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Ftun.png?alt=media&token=a15e09c6-cb4e-4016-a6da-225e20ab1825",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Австралия",
        group = "D",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Faus.png?alt=media&token=c4ed0ddc-abaa-43c0-9276-03d79b8989d9",
        sequenceNumber = 4
    ),
    TeamModel(
        team = "Испания",
        group = "E",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fesp.png?alt=media&token=b33d8cb6-087a-4da1-a56a-6e5c5fc98e9e",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Германия",
        group = "E",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fger.png?alt=media&token=233a894d-2542-40db-b409-197568244718",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "Япония",
        group = "E",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fjpn.png?alt=media&token=b63a0d30-497a-4468-ae11-cbd6204a26c0",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Коста-Рика",
        group = "E",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fcrc.png?alt=media&token=a504d4b6-49fb-4861-affc-1b0fe7eecf3c",
        sequenceNumber = 4
    ),
    TeamModel(
        team = "Бельгия",
        group = "F",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fbel.png?alt=media&token=e4eab895-1783-49ba-bc8e-5dab8734065f",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Канада",
        group = "F",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fcan.png?alt=media&token=00e0c7b0-6016-4649-a0b5-0917c87aacd3",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "Марокко",
        group = "F",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fmar.png?alt=media&token=35e6169d-ad7e-47e8-9f59-c373b5a0a452",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Хорватия",
        group = "F",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fcro.png?alt=media&token=a5b98c0f-3a2c-4423-8d75-a2985ff98f22",
        sequenceNumber = 4
    ),
    TeamModel(
        team = "Бразилия",
        group = "G",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fbra.png?alt=media&token=4a6e10ce-bcf0-4afe-a77f-f29f0fc1deb2",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Сербия",
        group = "G",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fsrb.png?alt=media&token=b6e2a14a-fe5b-473f-a7d3-55fe2f8163ae",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "Швейцария",
        group = "G",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fsui.png?alt=media&token=9d329b2f-53eb-4f67-b296-2fce02199cee",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Камерун",
        group = "G",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fcmr.png?alt=media&token=7dd531fd-3060-42b7-b73c-8bd41686abea",
        sequenceNumber = 4
    ),
    TeamModel(
        team = "Португалия",
        group = "H",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fpor.png?alt=media&token=e5e07c3b-c17f-4f30-8c36-5be4c1a21c26",
        sequenceNumber = 1
    ),
    TeamModel(
        team = "Гана",
        group = "H",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fgha.png?alt=media&token=1af9c25c-44ef-44d8-bf8c-3e40fc21f92a",
        sequenceNumber = 2
    ),
    TeamModel(
        team = "Уругвай",
        group = "H",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Furu.png?alt=media&token=917d436c-854b-403b-8475-74ae620ea500",
        sequenceNumber = 3
    ),
    TeamModel(
        team = "Южная Корея",
        group = "H",
        flagUrl = "https://firebasestorage.googleapis.com/v0/b/tote-test-923c5.appspot.com/o/flags%2Fkor.png?alt=media&token=f529c246-b7c4-4781-91b6-598e8c8e8261",
        sequenceNumber = 4
    ),
)

val SCHEDULER = arrayListOf<GameModel>(
    GameModel(
        id = 1,
        group = "A",
        team1 = "Катар",
        team2 = "Эквадор",
        start = convertDateTimeToTimestamp("20.11.2022 19:00")
    ),
    GameModel(
        id = 2,
        group = "B",
        team1 = "Англия",
        team2 = "Иран",
        start = convertDateTimeToTimestamp("21.11.2022 16:00")
    ),
    GameModel(
        id = 3,
        group = "A",
        team1 = "Сенегал",
        team2 = "Нидерланды",
        start = convertDateTimeToTimestamp("21.11.2022 19:00")
    ),
    GameModel(
        id = 4,
        group = "B",
        team1 = "США",
        team2 = "Уэльс",
        start = convertDateTimeToTimestamp("21.11.2022 22:00")
    ),
    GameModel(
        id = 5,
        group = "C",
        team1 = "Аргентина",
        team2 = "Саудовская Аравия",
        start = convertDateTimeToTimestamp("22.11.2022 13:00")
    ),
    GameModel(
        id = 6,
        group = "D",
        team1 = "Дания",
        team2 = "Тунис",
        start = convertDateTimeToTimestamp("22.11.2022 16:00")
    ),
    GameModel(
        id = 7,
        group = "C",
        team1 = "Мексика",
        team2 = "Польша",
        start = convertDateTimeToTimestamp("22.11.2022 19:00")
    ),
    GameModel(
        id = 8,
        group = "D",
        team1 = "Франция",
        team2 = "Австралия",
        start = convertDateTimeToTimestamp("22.11.2022 22:00")
    ),
    GameModel(
        id = 9,
        group = "F",
        team1 = "Марокко",
        team2 = "Хорватия",
        start = convertDateTimeToTimestamp("23.11.2022 13:00")
    ),
    GameModel(
        id = 10,
        group = "E",
        team1 = "Германия",
        team2 = "Япония",
        start = convertDateTimeToTimestamp("23.11.2022 16:00")
    ),
    GameModel(
        id = 11,
        group = "E",
        team1 = "Испания",
        team2 = "Коста-Рика",
        start = convertDateTimeToTimestamp("23.11.2022 19:00")
    ),
    GameModel(
        id = 12,
        group = "F",
        team1 = "Бельгия",
        team2 = "Канада",
        start = convertDateTimeToTimestamp("23.11.2022 22:00")
    ),
    GameModel(
        id = 13,
        group = "G",
        team1 = "Швейцария",
        team2 = "Камерун",
        start = convertDateTimeToTimestamp("24.11.2022 13:00")
    ),
    GameModel(
        id = 14,
        group = "H",
        team1 = "Уругвай",
        team2 = "Южная Корея",
        start = convertDateTimeToTimestamp("24.11.2022 16:00")
    ),
)
