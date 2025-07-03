// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorCommunityPrintThermal",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorCommunityPrintThermal",
            targets: ["PrintThermalPluginPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "PrintThermalPluginPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/PrintThermalPluginPlugin"),
        .testTarget(
            name: "PrintThermalPluginPluginTests",
            dependencies: ["PrintThermalPluginPlugin"],
            path: "ios/Tests/PrintThermalPluginPluginTests")
    ]
)