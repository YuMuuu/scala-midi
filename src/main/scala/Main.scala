import Main.devices
import javax.sound.midi.{MidiDevice, MidiSystem, MidiUnavailableException, Sequencer, Synthesizer}
import midi.MyMidiReceiver

import scala.util.{Failure, Success, Try, Using}

object Main extends App{

  val DEVICE_IN = 1



  val infos = MidiSystem.getMidiDeviceInfo().toList
  val devices: Try[List[MidiDevice]] = Try {infos.map(MidiSystem.getMidiDevice)}

  devices match {
    case Success(i) => println(i)
    case Failure(t) => t.printStackTrace()
  }

  devices match {
    case Failure(e: SecurityException) => println(e)
    case Failure(e: MidiUnavailableException) => println(e)
    case Failure(e) => println(e)
    case Success(devices) => {
      if (devices.length == 0) println("no devices")
      else {
        devices.zipWithIndex.foreach { case (device, num) => dumpDeviceInfo(device, num) }
        println("enter your midi device number >")
        val deviceNum = io.StdIn.readLine().toInt
        println(deviceNum)
        val device = devices(deviceNum)
        val receiver = new MyMidiReceiver
        device.open()
        device.getTransmitter().setReceiver(receiver)
        println("enter to exit >")
        io.StdIn.readLine()
        device.close()
        receiver.close()
      }
    }
  }


  def dumpDeviceInfo(device: MidiDevice, num: Int): Unit = {
    val info: MidiDevice.Info = device.getDeviceInfo()
    println(s"[${num}] devinfo: " + info.toString())
    println("  name:"        + info.getName())
    println("  vendor:"      + info.getVendor())
    println("  version:"     + info.getVersion())
    println("  description:" + info.getDescription())
    if (device.isInstanceOf[Synthesizer]) {
      println("  SYNTHESIZER")
    }
    if (device.isInstanceOf[Sequencer]) {
      println("  SEQUENCER")
    }
    println("")
  }



}
