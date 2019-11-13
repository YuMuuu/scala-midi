package midi

import javax.sound.midi.{MidiMessage, Receiver}

import scala.collection.mutable
import scala.io.AnsiColor._

class MyMidiReceiver extends Receiver {

  private val NoteOn = 144 until 144+16
  private val NoteOff = 128 until 128+16

  private val map: mutable.Map[MidiNoteNumber, MidiNoteVelNumber] = new mutable.HashMap

  override def close() = ()

  override def send(message: MidiMessage, timeStamp: Long): Unit = {
    val Array(_channelNumber: Int, _noteNumber: Int, _noteVelocity: Int) = message.getMessage.map(byte => (byte & 0xFF).toInt)

    val midiNoteNumber = MidiNoteNumber(_noteNumber)
    val midiVelNumber = MidiVelNumber(_noteVelocity)
    val midiNoteVelNumber = MidiNoteVelNumber(midiNoteNumber, midiVelNumber)


    if(NoteOn.contains(_channelNumber)) {
      map.update(midiNoteNumber, midiNoteVelNumber)
    }
    else if(NoteOff.contains(_channelNumber)) {
      map.remove(midiNoteNumber)
    }
    println(map.values.map(_.color).mkString(" "))
  }

}

case class MidiNoteNumber(value: Int)
case class MidiVelNumber(value: Int){
  override def toString: String = {
    REVERSED + RED + value.toString + RESET
  }
}
case class MidiNoteVelNumber(midiNoteNumber: MidiNoteNumber, midiVelNumber: MidiVelNumber){
  def color:String = {
    val fixedColor = midiVelNumber.value match {
      case i if (0 until 16)contains(i) => BLACK
      case i if (16 until 32)contains(i) => RED
      case i if (32 until 48)contains((i)) => GREEN
      case i if (48 until 64)contains((i)) => YELLOW
      case i if (64 until 80)contains((i)) => BLUE
      case i if (80 until 96)contains((i)) => MAGENTA
      case i if (96 until 112)contains((i)) => CYAN
      case i if (112 until 128)contains((i)) => WHITE
    }
    REVERSED + fixedColor + midiNoteNumber.value + RESET
  }
}

