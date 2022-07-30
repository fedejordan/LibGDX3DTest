package com.fedejordan.libgdx3dtest

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.utils.JsonReader


class LibGDX3DTest: ApplicationAdapter() {
    private lateinit var camera: PerspectiveCamera
    private lateinit var modelBatch: ModelBatch
    private lateinit var carModel: Model
    private lateinit var carModelInstance: ModelInstance
    private lateinit var environment: Environment
    private lateinit var cameraInputController: CameraInputController

    override fun create() {
        super.create()
        createCamera()
        createCameraInput()
        adjustCameraPosition()
        adjustCameraRange()
        createModelBatch()
        createCarModel()
        createCarModelInstance()
        createEnvironment()
    }

    override fun render() {
        super.render()
        clearScreen()
        updateCamera()
        renderModelBatch()
    }

    override fun dispose() {
        super.dispose()
        disposeModelBatch()
        disposeCubeModel()
    }

    // Utils - Create

    private fun createCamera() {
        camera = PerspectiveCamera(75f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }

    private fun createCameraInput() {
        cameraInputController = CameraInputController(camera)
        Gdx.input.inputProcessor = cameraInputController
    }

    private fun adjustCameraPosition() {
        camera.position?.set(50f, 200f, 200f)
        camera.lookAt(0f,0f,0f)
    }

    private fun adjustCameraRange() {
        camera.near = 0.1f
        camera.far = 300f
    }

    private fun createModelBatch() {
        modelBatch = ModelBatch()
    }

    private fun createCubeModel() {
        val modelBuilder = ModelBuilder()
        val attributes = (Usage.Position or Usage.Normal).toLong()
        val material = Material(ColorAttribute.createDiffuse(Color.RED))
        carModel = G3dModelLoader(JsonReader()).loadModel(Gdx.files.internal("model.g3dj"))
    }

    private fun createCubeModelInstance() {
        carModelInstance = ModelInstance(carModel, 0f, 0f, 0f)
    }

    private fun createEnvironment() {
        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.0f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    // Utils - render

    private fun clearScreen() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        val GL_DEPTH_BUFFER_BIT = 256
        val GL_COLOR_BUFFER_BIT = 16384
        Gdx.gl.glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)
    }

    private fun updateCamera() {
        camera.update()
        cameraInputController.update()
    }

    private fun renderModelBatch() {
        modelBatch.begin(camera)
        modelBatch.render(carModelInstance, environment)
        modelBatch.end()
    }

    // Utils - Dispose

    private fun disposeModelBatch() {
        modelBatch.dispose()
    }

    private fun disposeCubeModel() {
        carModel.dispose()
    }
}