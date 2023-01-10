package promag.groupe.proapp


const val USER_PREFERENCE = "user_data"
const val USER_TOKEN = "token"
const val ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.ssssss'Z'"
const val DISCUSSION_EXTRA = "DISCUSSION"
const val PRODUCT_EXTRA = "PRODUCT"
const val TIER_EXTRA = "TIER"
const val PAYMENT_EXTRA = "PAYMENT"
const val TASK_EXTRA = "TASK"


class Environment{

    private var host = "http://10.0.0.21:8000"

    constructor(env: String){
        host = if(env == "dev") "http://10.0.0.21:8000/"
        else {
            "https://mini-erp-beta.vercel.app/"
        }
    }

    val BASE_URL : String
        get(){
            return host
        }

    val BASE_URL_API : String
        get(){
            return BASE_URL + "api/"
        }

    val APP_AUTH_TOKEN_URL : String
        get(){
            return BASE_URL+"api/auth/token/"
        }

    val APP_AUTH_USER_URL : String
        get(){
            return BASE_URL+"api/auth/username/"
        }


}
