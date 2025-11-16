// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "LightsaberApp",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .executable(name: "LightsaberApp", targets: ["LightsaberApp"])
    ],
    dependencies: [
    ],
    targets: [
        .executableTarget(
            name: "LightsaberApp",
            path: "Sources/LightsaberApp"
        )
    ]
)
