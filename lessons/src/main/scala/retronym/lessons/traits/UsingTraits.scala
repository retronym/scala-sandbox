package retronym.lessons.traits

import _root_.org.spex.Specification


object UsingTraits extends Specification {
  trait Camera {
    def shoot(exposure: Int)
  }
  trait Zoomable {
    def zoomIn(level: Int): Int
  }

  "Traits as interface contract" should {
    "Implement one interface" in {
      class CameraImpl extends Camera {
        def shoot(exposure: Int) = {
          println("shooting...");
          Thread.sleep(exposure)
        }
      }

      val c: Camera = new CameraImpl
      c.shoot(10) must be_==(())
    }

    "implement multiple interfaces, anonymous class" in {

      val c: Camera with Zoomable = new Camera with Zoomable {
        var zoomLevel = 5

        def shoot(exposure: Int) = println("shoot")

        def zoomIn(level: Int) = {
          zoomLevel = zoomLevel + level
          zoomLevel
        }
      }
      c.zoomIn(1) must be_==(6)
    }

    "one trait combining multiple interfaces, anonymous class" in {
      trait ZoomableCamera extends Camera with Zoomable

      val c: ZoomableCamera = new ZoomableCamera {
        var zoomLevel = 5

        def shoot(exposure: Int) = println("shoot")

        def zoomIn(level: Int) = {
          zoomLevel = zoomLevel + level
          zoomLevel
        }
      }
      c.zoomIn(1) must be_==(6)
    }

    "type alias for multiple interfaces, anonymous class" in {
      object MyTypeHolder {
        type ZoomableCamera = Camera with Zoomable
      }
      import MyTypeHolder.ZoomableCamera
      val c: ZoomableCamera = new Camera with Zoomable {
        var zoomLevel = 5

        def shoot(exposure: Int) = println("shoot")

        def zoomIn(level: Int) = {
          zoomLevel = zoomLevel + level
          zoomLevel
        }
      }
      c.zoomIn(1) must be_==(6)
    }
  }
}
