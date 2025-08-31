package com.quizle.presentation.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable


object FilterParam{
    const val FILTER_TYPE_TOP_VIEWED = "TOP_VIEWED"
    const val FILTER_TYPE_POPULAR = "POPULAR"
}

@Serializable
sealed interface GraphRoute{
    @Serializable
    data object Auth: GraphRoute
    @Serializable
    data object Dashboard: GraphRoute
}

@Serializable
sealed interface AuthRoute {

    @Serializable
    data object Login: AuthRoute
    @Serializable
    data object SignUp: AuthRoute
    @Serializable
    data object Splash : AuthRoute

}

@Serializable
sealed class NavBottomRoute(val route: String) {
    /** Navigation Bottom **/
    @Serializable
    data object Home : NavBottomRoute("Home")
    @Serializable
    data object Settings : NavBottomRoute("Settings")
    @Serializable
    data object History : NavBottomRoute("History")
    @Serializable
    data object Topic : NavBottomRoute("Topic")
    @Serializable
    data object Favorite : NavBottomRoute("Favorite")
}


@Serializable
sealed interface DashboardRoute {

    /** Rest of screens **/
    @Serializable
    data class IssueReport(val questionId: String): DashboardRoute
    @Serializable
    data class Quiz(val topicId: String, val quizTimeEnabled: Boolean): DashboardRoute
    @Serializable
    data object Result: DashboardRoute
    @Serializable
    data object Notifications : DashboardRoute
    @Serializable
    data object Profile : DashboardRoute

}




fun NavController.back(){
    popBackStack()
}

fun NavController.navigateToIssueReport(questionId: String){
    navigate(route = DashboardRoute.IssueReport(questionId))
}

fun NavController.navigateToNotifications(){
    navigate(route = DashboardRoute.Notifications)
}



fun NavController.navigateToQuiz(
    topicId: String,
    quizTimeEnabled: Boolean = true
){

    navigate(route = DashboardRoute.Quiz(topicId, quizTimeEnabled)) {
        popUpTo(route = DashboardRoute.Quiz(topicId, quizTimeEnabled)) {
            inclusive = true
        }
    }
}

//fun NavController.navigateToHome(){
//    navigate(route = NavBottomRoute.Home){
//
//    }
//}

fun NavController.navigateToProfile(){
    navigate(route = DashboardRoute.Profile){

    }
}

fun NavController.navigateToResult(){
    navigate(route = DashboardRoute.Result){
//        popUpTo(graph.id) {
//            inclusive = true
//        }

        popUpTo<DashboardRoute.Quiz>{
            inclusive = true
        }
    }
}

fun NavController.navigateToLogin(){
    navigate(route = AuthRoute.Login){
        popUpTo(graph.id) { // This pops up to the root NavGraph itself
            inclusive = true // This ensures the root graph's initial destination is also popped
        }
    }
}


fun NavController.navigateToSignUp(){
    navigate(route = AuthRoute.SignUp){
        popUpTo(graph.id) { // This pops up to the root NavGraph itself
            inclusive = true // This ensures the root graph's initial destination is also popped
        }
    }
}

fun NavController.navigateToDashboard(){
    navigate(route = GraphRoute.Dashboard){

    }
}



//fun NavController.navigateToLogin(){
//    navigate(route = Route.Login){
//        popUpTo<Route.SignUp>{
//            inclusive = true
//        }
//
//        launchSingleTop = true
//    }
//}
//
//fun NavController.navigateToSignUp(){
//    navigate(route = Route.SignUp){
//        popUpTo<Route.Login>{
//            inclusive = true
//        }
//
//        launchSingleTop = true
//    }
//}



