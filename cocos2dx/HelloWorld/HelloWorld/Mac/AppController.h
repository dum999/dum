//
//  AppController.h
//  HelloWorld
//
//  Created by Jirapong Charoentim on 2/13/55 BE.
//  Copyright (c) 2555 Jirapong. All rights reserved.
//

#import <UIKit/UIKit.h>

@class RootViewController;

@interface AppController : NSObject <UIApplicationDelegate> {
	UIWindow			*window;
	RootViewController	*viewController;
}

@property (nonatomic,readwrite,retain) UIWindow *window;
@property (nonatomic,readonly,retain) RootViewController *viewController;

@end
