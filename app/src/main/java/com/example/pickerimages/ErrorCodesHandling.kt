package com.example.pickerimages

import com.google.firebase.database.DatabaseError
import com.google.firebase.storage.StorageException

class ErrorCodesHandling {

    fun convertStorageErrorsToMessages(exception: String?): String {
        return when (exception) {
            StorageException.ERROR_BUCKET_NOT_FOUND.toString()-> "عذرا البيانات المطلوبة غير موجودة."
            StorageException.ERROR_CANCELED.toString() -> "تم إلغاء العملية."
            StorageException.ERROR_INVALID_CHECKSUM.toString() -> "الملف الموجود على العميل لا يتطابق مع المجموع الاختباري للملف الذي تلقاه الخادم."
            StorageException.ERROR_NOT_AUTHENTICATED.toString() -> "لم تتم مصادقة المستخدم، يرجى المصادقة والمحاولة مرة أخرى."
            StorageException.ERROR_OBJECT_NOT_FOUND.toString() -> "عذرا البيانات المطلوبة غير موجودة."
            StorageException.ERROR_PROJECT_NOT_FOUND.toString() -> "المشروع المحدد غير موجود."
            StorageException.ERROR_QUOTA_EXCEEDED.toString() -> "لقد تم تجاوز الحصة المخصصة لمساحه التخزين السحابي الخاصة بك."
            StorageException.ERROR_RETRY_LIMIT_EXCEEDED.toString() -> "تجاوزت العملية الحد الأقصى لعدد مرات إعادة المحاولة."
            StorageException.ERROR_UNKNOWN.toString() -> "حدث خطأ غير معروف. الرجاء معاودة المحاولة في وقت لاحق."
            StorageException.ERROR_NOT_AUTHORIZED.toString()->"عذرًا، ليس لديك الصلاحيات الكافية للقيام بهذه العملية. يرجى التحقق من صلاحيات حسابك والمحاولة مرة أخرى."
            else -> "حدث خطأ غير معروف في قواعد البيانات. يرجى المحاولة مرة أخرى لاحقًا."

//            "ERROR_WRITE_CANCELED" -> "تم إلغاء الكتابة، عادةً نتيجة لدخول التطبيق إلى الخلفية."
//            "ERROR_NO_SUCH_BUCKET" -> "لم يتم تكوين أي قواعد بيانات"
//            "ERROR_INVALID_URL" -> "عنوان URL للتنزيل غير صالح."
//            "ERROR_INVALID_ARGUMENT" -> "تم توفير متطلبات غير صالحة."
//            "ERROR_UNHANDLED_RESPONSE_CODE" -> "استجابة HTTP لم يتم إدارتها."
//            "ERROR_UNAUTHORIZED" -> "غير مصرح للعميل بقراءة أو الكتابة إلى البيانات المحددة."
//            "ERROR_REQUIRES_RECENT_LOGIN" -> "تتطلب هذه العملية تسجيل دخول حديث. الرجاد الدخول على الحساب من جديد."
//            "ERROR_NOT_ENOUGH_QUOTA" -> "ليس لدى المستخدم الأذونات الكافية لهذه العملية."
//            "ERROR_NOT_FOUND" -> "عذرا البيانات المطلوبة غير موجودة."
        }
    }




    fun convertRealtimeDatabaseErrorsToMessages(exception: String?): String {
        return when (exception) {
            DatabaseError.DATA_STALE.toString() -> "قام عميل آخر بتعديل البيانات بعد أن قرأها العميل الحالي."
            DatabaseError.DISCONNECTED.toString() -> "فقد العميل الاتصال بقاعدة البيانات"
            DatabaseError.EXPIRED_TOKEN.toString() -> "رمز المصادقة المميز المستخدم للوصول إلى قاعدة البيانات لم يعد صالحًا."
            DatabaseError.INVALID_TOKEN.toString() -> "لم يتم إنشاء رمز المصادقة بشكل صحيح أو تم العبث به."
            DatabaseError.MAX_RETRIES.toString() -> "تمت إعادة المحاولة كثيرًا"
            DatabaseError.NETWORK_ERROR.toString() -> "فقدان الاتصال بالشبكة."
            DatabaseError.OPERATION_FAILED.toString() -> "فشلت عملية قاعدة البيانات لسبب غير معروف."
            DatabaseError.OVERRIDDEN_BY_SET.toString() -> "تم تجاوز المعاملة من خلال مجموعة لاحقة"
            DatabaseError.PERMISSION_DENIED.toString() -> "ليس لدى هذا العميل الإذن بإجراء هذه العملية"
            DatabaseError.UNAVAILABLE.toString() -> "لا يمكن الوصول للخادم"
            DatabaseError.UNKNOWN_ERROR.toString() -> "حدث خطأ غير معروف. الرجاء معاودة المحاولة في وقت لاحق."
            DatabaseError.USER_CODE_EXCEPTION.toString() -> "حدث استثناء في رمز المستخدم"
            DatabaseError.WRITE_CANCELED.toString() -> "تم إلغاء عملية الكتابة"
            else -> "حدث خطأ غير معروف في قواعد البيانات. يرجى المحاولة مرة أخرى لاحقًا."
        }
    }
}