package com.quizle.di

import com.quizle.data.local.database.QuizleDatabase
import com.quizle.data.local.database.QuizleDatabaseFactory
import com.quizle.data.local.prefrences.AppPreferences
import com.quizle.data.remote.HttpClientFactory
import com.quizle.data.remote.data_source.app_info.KtorAppInfoDataSource
import com.quizle.data.remote.data_source.app_info.RemoteAppInfoDataSource
import com.quizle.data.remote.data_source.quiz.RemoteQuizDataSource
import com.quizle.data.remote.data_source.quiz.KtorQuizRemoteDataSource
import com.quizle.data.remote.data_source.user.RemoteUserDataSource
import com.quizle.data.respository.IssueReportRepositoryImpl
import com.quizle.data.respository.TopicRepositoryImpl
import com.quizle.data.respository.QuestionRepositoryImpl
import com.quizle.data.respository.UserRepositoryImpl
import com.quizle.domain.repository.IssueReportRepository
import com.quizle.domain.repository.QuestionRepository
import com.quizle.domain.repository.TopicRepository
import com.quizle.domain.repository.UserRepository
import com.quizle.presentation.dashboard.DashboardViewModel
import com.quizle.presentation.screens.issue_report.IssueReportViewModel
import com.quizle.data.remote.data_source.user.KtorUserRemoteDataSource
import com.quizle.data.respository.AppReleaseInfoRepositoryImpl
import com.quizle.data.respository.QuizResultRepositoryImpl
import com.quizle.data.utils.FileReader
import com.quizle.domain.repository.AppReleaseInfoRepository
import com.quizle.domain.repository.QuizResultRepository
import com.quizle.domain.utils.DefaultResourceProvider
import com.quizle.domain.utils.ResourceProvider
import com.quizle.presentation.screens.history.HistoryViewModel
import com.quizle.presentation.screens.home.HomeViewModel
import com.quizle.presentation.screens.login.LoginViewModel
import com.quizle.presentation.screens.profile.ProfileViewModel
import com.quizle.presentation.screens.quiz.QuizViewModel
import com.quizle.presentation.screens.result.ResultViewModel
import com.quizle.presentation.screens.settings.SettingsViewModel
import com.quizle.presentation.screens.sign_up.SignUpViewModel
import com.quizle.presentation.screens.splash_screen.SplashViewModel
import com.quizle.presentation.screens.topic.TopicViewModel
import com.quizle.presentation.util.AppVersionHelper
import com.quizle.presentation.util.Validator
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinModule = module {

    single { HttpClientFactory.create() }
    single { QuizleDatabaseFactory.create(context = get())}
    single { get<QuizleDatabase>().getQuizTopicDao()}
    single { get<QuizleDatabase>().getQuizQuestionDao()}
    single { get<QuizleDatabase>().getUserAnswerDao()}
    single { get<QuizleDatabase>().getUserDao()}
    single { get<QuizleDatabase>().getQuizResultDao()}



    singleOf(::QuestionRepositoryImpl).bind<QuestionRepository>()
    singleOf(::TopicRepositoryImpl).bind<TopicRepository>()
    singleOf(::KtorQuizRemoteDataSource).bind<RemoteQuizDataSource>()
    singleOf(::KtorUserRemoteDataSource).bind<RemoteUserDataSource>()
    singleOf(::KtorAppInfoDataSource).bind<RemoteAppInfoDataSource>()
    singleOf(::AppReleaseInfoRepositoryImpl).bind<AppReleaseInfoRepository>()
    singleOf(::IssueReportRepositoryImpl).bind<IssueReportRepository>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::QuizResultRepositoryImpl).bind<QuizResultRepository>()


    viewModelOf(::QuizViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::IssueReportViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::TopicViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)





    single { Validator(context = get()) }
    single { AppPreferences(context = get()) }
    single { AppVersionHelper(context = get()) }
    single { FileReader(context = get()) }
    single <ResourceProvider> { DefaultResourceProvider(context = get()) }

}