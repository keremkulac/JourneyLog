package com.keremkulac.journeylog.util

import android.content.Context
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.Vehicle
import javax.inject.Inject

class InputValidation @Inject constructor(private val context: Context) {
    private val regex = "^-?\\d*(\\.\\d+)?$".toRegex()

    fun validateEmailAndPassword(
        userEmail: String?,
        userPassword: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            userEmail.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_email))
                false
            }

            userPassword.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_password))
                false
            }

            userEmail.isNotEmpty() -> {
                isValidEmail(userEmail) { message ->
                    validationMessage(message)
                }
            }

            else -> true
        }
    }

    fun validateUser(
        userName: String?,
        userLastname: String?,
        userEmail: String?,
        userPassword: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            userName.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_name))

                false
            }

            userLastname.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_surname))
                false
            }

            userEmail.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_email))
                false
            }

            userPassword.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_password))
                false
            }

            userEmail.isNotEmpty() -> {
                isValidEmail(userEmail) { message ->
                    validationMessage(message)
                }
            }

            else -> true
        }
    }

    fun isValidEmail(email: String?, validationMessage: (String) -> Unit): Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            return true
        } else {
            validationMessage(context.getString(R.string.validation_message_valid_email))
            return false
        }

    }


    fun validateUpdatePassword(
        oldPassword: String?,
        newPassword: String?,
        confirmPassword: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            oldPassword.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_old_password_not_empty_message))
                false
            }

            newPassword.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_new_password_not_empty_message))
                false
            }

            confirmPassword.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_new_password_confirm_not_empty_message))
                false
            }

            oldPassword.length < 6 -> {
                validationMessage(context.getString(R.string.warning_old_password_must_6_character_message))
                false
            }

            newPassword.length < 6 -> {
                validationMessage(context.getString(R.string.warning_new_password_must_6_character_message))
                false
            }

            confirmPassword.length < 6 -> {
                validationMessage(context.getString(R.string.warning_new_password_confirm_not_empty_message))
                false
            }

            newPassword != confirmPassword -> {
                validationMessage(context.getString(R.string.warning_passwords_must_be_same_message))
                false
            }

            else -> true
        }

    }

    fun validateFuelInputReceipt(
        stationName: String?,
        fuelType: String?,
        literPrice: String?,
        liter: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            stationName.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_receipt_station_name_not_empty_message))
                false
            }

            fuelType.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_receipt_fuel_type_empty_message))
                false
            }

            literPrice.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_receipt_liter_price_empty_message))
                false
            }

            !regex.matches(literPrice) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            liter.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_receipt_liter_empty_message))
                false
            }

            !regex.matches(liter) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            else -> true
        }
    }

    fun validateVehicleInputReceipt(
        vehicleLicensePlate: String?,
        vehicleLastKm: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            vehicleLicensePlate.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_receipt_vehicle_license_plate_message))
                false
            }

            vehicleLastKm.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.warning_receipt_vehicle_last_km_message))
                false
            }

            !regex.matches(vehicleLastKm) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            else -> true
        }
    }


    fun validateUpdateUser(
        userName: String?,
        userLastname: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            userName.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_name))
                false
            }

            userLastname.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_surname))
                false
            }

            else -> true
        }
    }

    fun validateLicensePlate(
        selectedVehicleType: String?,
        licensePlate: String?,
        lastKm: String?,
        selectedFuelType: String?,
        per100KilometerFuel: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {

            selectedVehicleType.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_vehicle))
                false
            }

            licensePlate.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_license_plate))
                false
            }

            licensePlate.length < 9 || licensePlate.length > 10 -> {
                validationMessage(context.getString(R.string.validation_message_valid_license_plate))
                false
            }

            lastKm.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_vehicle_last_km))
                false
            }

            !regex.matches(lastKm) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            selectedFuelType.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_fuel_type))
                false
            }

            per100KilometerFuel.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_per_100_kilometer_fuel))
                false
            }

            !regex.matches(per100KilometerFuel) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            else -> true
        }

    }

    fun validateOwnTripCostFuelCalculation(
        selectedVehicle: Vehicle?,
        distanceToTrip: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            selectedVehicle == null -> {
                validationMessage(context.getString(R.string.validation_message_empty_license_plate))
                false
            }

            distanceToTrip.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_distance))
                false
            }

            !regex.matches(distanceToTrip) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            else -> true
        }
    }

    fun validateOtherTripCostFuelCalculation(
        averageFuelPrice: AverageFuelPrice?, distanceToTrip: String?,
        vehicleUsedFuelPer100Kilometers: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            distanceToTrip.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_distance))
                false
            }

            !regex.matches(distanceToTrip) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            averageFuelPrice == null -> {
                validationMessage(context.getString(R.string.validation_message_empty_fuel_type))
                false
            }

            vehicleUsedFuelPer100Kilometers.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_per_100_kilometer_fuel))
                false
            }

            !regex.matches(vehicleUsedFuelPer100Kilometers) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            else -> true
        }
    }

    fun validateFuelConsumption(
        per100KilometerFuel: String?,
        selectedFuelType: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {

            per100KilometerFuel.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_per_100_kilometer_fuel))
                false
            }

            !regex.matches(per100KilometerFuel) -> {
                validationMessage(context.getString(R.string.validation_message_enter_only_number))
                false
            }

            selectedFuelType.isNullOrEmpty() -> {
                validationMessage(context.getString(R.string.validation_message_empty_fuel_type))
                false
            }

            else -> true
        }
    }

}