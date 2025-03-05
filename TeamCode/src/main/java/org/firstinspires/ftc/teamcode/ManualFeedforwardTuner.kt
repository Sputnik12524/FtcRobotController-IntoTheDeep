package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.TimeProfile
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.constantProfile
import com.acmerobotics.roadrunner.ftc.DriveViewFactory
import com.acmerobotics.roadrunner.ftc.RollingThreeMedian
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

class ManualFeedforwardTuner(val dvf: DriveViewFactory) : LinearOpMode() {
    companion object {
        @JvmField
        var DISTANCE = 32.0
    }

    enum class Mode {
        DRIVER_MODE,
        TUNING_MODE
    }

    override fun runOpMode() {
        val telemetry = MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().telemetry)

        val view = dvf.make(hardwareMap)
        val profile = TimeProfile(
            constantProfile(
            DISTANCE, 0.0, view.maxVel, view.minAccel, view.maxAccel).baseProfile)

        var mode = Mode.TUNING_MODE

        telemetry.addLine("Ready!")
        telemetry.update()
        telemetry.clearAll()

        waitForStart()

        if (isStopRequested) return

        var movingForwards = true
        var startTs = System.nanoTime() / 1e9

        val lastPositions = MutableList(view.forwardEncs.size) { 0 }
        val lastTimes = view.forwardEncs.map { ElapsedTime() }
        val velEstimates = view.forwardEncs.map { RollingThreeMedian() }
        while (!isStopRequested) {
            telemetry.addData("mode", mode)

            when (mode) {
                Mode.TUNING_MODE -> {
                    if (gamepad1.y) {
                        mode = Mode.DRIVER_MODE
                    }

                    for (g in view.encoderGroups) {
                        g.bulkRead()
                    }

                    for (i in view.forwardEncs.indices) {
                        val ref = view.forwardEncs[i]

                        val pv = view.encoder(ref).getPositionAndVelocity()
                        val v = if (pv.velocity == null) {
                            val lastPos = lastPositions[i]
                            lastPositions[i] = pv.position
                            lastTimes[i].reset()
                            velEstimates[i].update((pv.position - lastPos) / lastTimes[i].seconds())
                        } else {
                            pv.velocity!!.toDouble()
                        }

                        telemetry.addData("v$i", view.inPerTick * v)
                    }

                    val ts = System.nanoTime() / 1e9
                    val t = ts - startTs
                    if (t > profile.duration) {
                        movingForwards = !movingForwards
                        startTs = ts
                    }

                    var v = profile[t].drop(1)
                    if (!movingForwards) {
                        v = v.unaryMinus()
                    }
                    telemetry.addData("vref", v[0])

                    val power = view.feedforwardFactory.make().compute(v) / view.voltageSensor.voltage
                    view.setDrivePowers(PoseVelocity2d(Vector2d(power, 0.0), 0.0))
                }
                Mode.DRIVER_MODE -> {
                    if (gamepad1.b) {
                        mode = Mode.TUNING_MODE
                        movingForwards = true
                        startTs = System.nanoTime() / 1e9
                    }

                    view.setDrivePowers(
                        PoseVelocity2d(
                        Vector2d(
                            -gamepad1.left_stick_y.toDouble(),
                            -gamepad1.left_stick_x.toDouble()
                        ),
                        -gamepad1.right_stick_x.toDouble()
                    )
                    )
                }
            }

            telemetry.update()
        }
    }
}

