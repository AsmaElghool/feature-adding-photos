package com.example.pickerimages

import com.google.firebase.database.DatabaseError
import com.google.firebase.storage.StorageException

class ErrorCodesHandling {

    fun convertStorageErrorsToMessages(exception: Int?): String {
        return when (exception) {
            StorageException.ERROR_BUCKET_NOT_FOUND-> "عذرا البيانات المطلوبة غير موجودة."
            StorageException.ERROR_CANCELED -> "تم إلغاء العملية."
            StorageException.ERROR_INVALID_CHECKSUM -> "الملف الموجود على العميل لا يتطابق مع المجموع الاختباري للملف الذي تلقاه الخادم."
            StorageException.ERROR_NOT_AUTHENTICATED -> "لم تتم مصادقة المستخدم، يرجى المصادقة والمحاولة مرة أخرى."
            StorageException.ERROR_OBJECT_NOT_FOUND -> "عذرا البيانات المطلوبة غير موجودة."
            StorageException.ERROR_PROJECT_NOT_FOUND -> "المشروع المحدد غير موجود."
            StorageException.ERROR_QUOTA_EXCEEDED -> "لقد تم تجاوز الحصة المخصصة لمساحه التخزين السحابي الخاصة بك."
            StorageException.ERROR_RETRY_LIMIT_EXCEEDED-> "تجاوزت العملية الحد الأقصى لعدد مرات إعادة المحاولة."
            StorageException.ERROR_UNKNOWN -> "حدث خطأ غير معروف. الرجاء معاودة المحاولة في وقت لاحق."
            StorageException.ERROR_NOT_AUTHORIZED->"عذرًا، ليس لديك الصلاحيات الكافية للقيام بهذه العملية. يرجى التحقق من صلاحيات حسابك والمحاولة مرة أخرى."
            else -> "حدث خطأ غير معروف في قواعد البيانات. يرجى المحاولة مرة أخرى لاحقًا."
        }
    }

    fun convertRealtimeDatabaseErrorsToMessages(exception: Int?): String {
        return when (exception) {
            DatabaseError.DATA_STALE -> "قام عميل آخر بتعديل البيانات بعد أن قرأها العميل الحالي."
            DatabaseError.DISCONNECTED -> "فقد العميل الاتصال بقاعدة البيانات"
            DatabaseError.EXPIRED_TOKEN -> "رمز المصادقة المميز المستخدم للوصول إلى قاعدة البيانات لم يعد صالحًا."
            DatabaseError.INVALID_TOKEN -> "لم يتم إنشاء رمز المصادقة بشكل صحيح أو تم العبث به."
            DatabaseError.MAX_RETRIES -> "تمت إعادة المحاولة كثيرًا"
            DatabaseError.NETWORK_ERROR -> "فقدان الاتصال بالشبكة."
            DatabaseError.OPERATION_FAILED -> "فشلت عملية قاعدة البيانات لسبب غير معروف."
            DatabaseError.OVERRIDDEN_BY_SET -> "تم تجاوز المعاملة من خلال مجموعة لاحقة"
            DatabaseError.PERMISSION_DENIED -> "ليس لدى هذا العميل الإذن بإجراء هذه العملية"
            DatabaseError.UNAVAILABLE -> "لا يمكن الوصول للخادم"
            DatabaseError.UNKNOWN_ERROR -> "حدث خطأ غير معروف. الرجاء معاودة المحاولة في وقت لاحق."
            DatabaseError.USER_CODE_EXCEPTION -> "حدث استثناء في رمز المستخدم"
            DatabaseError.WRITE_CANCELED-> "تم إلغاء عملية الكتابة"
            else -> "حدث خطأ غير معروف في قواعد البيانات. يرجى المحاولة مرة أخرى لاحقًا."
        }
    }
}