// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorCommunityPrintThermal",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorCommunityPrintThermal",
            targets: ["PrintThermalPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "PrintThermalPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/PrintThermalPlugin"),
        .testTarget(
            name: "PrintThermalPluginTests",
            dependencies: ["PrintThermalPlugin"],
            path: "ios/Tests/PrintThermalPluginTests")
    ]
)
