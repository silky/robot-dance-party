(ns robot-dance-party.core
  (:use [overtone.live]
        [overtone.inst.sampled-piano]
        [robot-dance-party.music]
        [robot-dance-party.sphero]
        [robot-dance-party.roomba])
  (:require
            [ellipso.core :as core]
            [ellipso.commands :as commands])
  (:import roombacomm.RoombaCommSerial))

(declare roomba)
(declare sphero)

(comment
  ;; init the roomba
  (def roomba (RoombaCommSerial. ))

  ;;Find your port for your Roomba
  (map println (.listPorts roomba))

  (def portname "/dev/rfcomm0")
  (.connect roomba portname)
  (.startup roomba)  ;;puts Roomba in safe Mode
  ;; What mode is Roomba in?
  (.modeAsString roomba)
  (.control roomba)
  (.updateSensors roomba) ; returns true if you are connected
  (.playNote roomba 72 40)
  (.disconnect roomba)

  ;; init the sphero
  (def sphero (core/connect "/dev/rfcomm1"))

  (core/disconnect sphero)

  (commands/execute sphero (commands/colour 0xFF0000)) ;;red
  (commands/execute sphero (commands/colour 0xFF8000)) ;;yellow
) 


(defn go-play [beat song]
  (let [m (metro)]
    (reset! next-melody song)
    (bass-loop-player m)
    (robot-loop-player m)
    (roomba-loop-player m @roomba-moves)
    (sphero-loop-player m @sphero-moves)
    (melody-loop-player m song @next-melody)))


(defn all-stop []
  (stop)
  (.stop roomba)
  (commands/execute sphero (commands/roll 0x00 180)))


(comment

  (change-melody-sounds [])
  (change-bass-sounds [tick])
  (change-robot-sounds [])
  (go-play (metro) repetition-a)
  (change-robot-sounds [robot-ready])
  (change-melody-sounds [saw2])
  (change-robot-sounds [])
  (change-melody (transpose -5 repetition-a))
  (change-melody (transpose -10 repetition-a))
  (change-melody repetition-a)
  (change-melody repetition-b)
  (change-melody (transpose 2 repetition-b))
  (change-melody repetition-b)
  (change-melody (transpose -2 repetition-b))
  (change-melody repetition-b3)
  (change-melody (transpose -2 repetition-b3))
  (change-melody repetition-b)
  (change-melody repetition-a)


  (change-bass-sounds [dirty-kick])
  (change-melody-sounds [])

  (change-melody-sounds [piano])
  (change-bass-sounds [])
  (change-melody theme)
  (change-melody-sounds [])

  (change-bass-sounds [dirty-kick daft-kick])
  (change-melody-sounds [piano])

  (change-roomba-moves [ #(.spinRight %) #(.spinLeft %)])
  (change-sphero-moves (map commands/colour [RED YELLOW BLUE PURPLE]))
  (change-sphero-moves [(commands/roll 0x4B 0) (commands/roll 0x4B 180)])


  (all-stop))


