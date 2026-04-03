<template>
	<view class="worker-dashboard">
		<!-- 顶部状态概览 -->
		<view class="stats-card cu-list grid col-3 no-border shadow">
			<view class="cu-item">
				<view class="text-blue text-bold text-xxl">{{ stats.todayCount || 0 }}</view>
				<text>今日单数</text>
			</view>
			<view class="cu-item">
				<view class="text-orange text-bold text-xxl">{{ stats.pendingCount || 0 }}</view>
				<text>待处理</text>
			</view>
			<view class="cu-item">
				<view class="text-green text-bold text-xxl">{{ stats.rating || '5.0' }}</view>
				<text>服务评分</text>
			</view>
		</view>

		<!-- 任务选项卡 -->
		<view class="nav-tabs bg-white margin-top">
			<view class="cu-item flex-sub" :class="tabIndex==1?'text-blue cur':''" @tap="tabSelect(1)">
				<text class="cuIcon-tagfill"></text> 抢单池
			</view>
			<view class="cu-item flex-sub" :class="tabIndex==2?'text-blue cur':''" @tap="tabSelect(2)">
				<text class="cuIcon-timefill"></text> 我的任务
			</view>
		</view>

		<!-- 订单列表 -->
		<scroll-view scroll-y class="order-list margin-top-sm">
			<view v-if="orderList.length == 0" class="empty-box flex flex-direction align-center justify-center p-50">
				<image src="/static/images/empty.png" style="width: 200rpx;height: 200rpx;" mode="aspectFit"></image>
				<text class="text-gray margin-top">暂无相关订单</text>
			</view>

			<view v-for="(item, index) in orderList" :key="index" class="order-card margin-sm padding bg-white radius shadow">
				<view class="flex justify-between align-center border-bottom padding-bottom-xs margin-bottom-sm">
					<view class="text-bold text-lg">订单号：{{ item.orderNo }}</view>
					<view class="cu-tag round bg-orange light">{{ getStatusLabel(item.orderStatus) }}</view>
				</view>
				
				<view class="item-info">
					<view class="flex margin-bottom-xs">
						<text class="text-gray w-150">老人姓名：</text>
						<text class="text-black">{{ item.elderlyName }}</text>
					</view>
					<view class="flex margin-bottom-xs">
						<text class="text-gray w-150">服务项目：</text>
						<text class="text-blue">{{ item.itemName }}</text>
					</view>
					<view class="flex margin-bottom-xs">
						<text class="text-gray w-150">预约时间：</text>
						<text class="text-black">{{ item.serviceTime }}</text>
					</view>
					<view class="flex align-start margin-bottom-xs">
						<text class="text-gray w-150">服务地址：</text>
						<text class="text-black flex-sub">{{ item.serviceAddress }}</text>
					</view>
				</view>

				<!-- 操作按钮组 -->
				<view class="flex justify-end margin-top-sm border-top padding-top-sm">
					<button v-if="item.orderStatus == 1" @click="handleAccept(item.orderId)" class="cu-btn bg-blue round sm ghost">立即抢单</button>
					<button v-if="item.orderStatus == 2 && !item.startTime" @click="handleStart(item.orderId)" class="cu-btn bg-orange round sm">开始服务</button>
					<button v-if="item.orderStatus == 2" @click="handleComplete(item.orderId)" class="cu-btn bg-green round sm margin-left-sm">确认完成</button>
					<button @click="navToDetail(item.orderId)" class="cu-btn line-blue round sm margin-left-sm">查看详情</button>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import { listWorkerOrder, acceptOrder, startService, completeService } from "@/api/order/workerOrder"

	export default {
		data() {
			return {
				tabIndex: 1, // 1-抢单池, 2-我的任务
				stats: {
					todayCount: 0,
					pendingCount: 0,
					rating: '5.0'
				},
				orderList: []
			}
		},
		created() {
			this.loadData();
		},
		methods: {
			tabSelect(index) {
				this.tabIndex = index;
				this.loadData();
			},
			loadData() {
				const status = this.tabIndex == 1 ? 1 : null; // 抢单池只看待接单(1)
				listWorkerOrder({ orderStatus: status }).then(res => {
					this.orderList = res.rows;
					// 简单模拟统计数据
					if(this.tabIndex == 2) {
						this.stats.pendingCount = this.orderList.filter(o => o.orderStatus < 4).length;
					}
				});
			},
			getStatusLabel(status) {
				const map = {
					0: '待支付',
					1: '待派单',
					2: '服务中',
					3: '已完成',
					4: '已取消',
					5: '已评价',
					6: '退款中',
					7: '已退款'
				};
				return map[status] || '未知';
			},
			handleAccept(orderId) {
				this.$modal.confirm('确定要接此单吗？').then(() => {
					acceptOrder(orderId).then(() => {
						this.$modal.msgSuccess("抢单成功");
						this.loadData();
					});
				});
			},
			handleStart(orderId) {
				startService(orderId).then(() => {
					this.$modal.msgSuccess("开始服务");
					this.loadData();
				});
			},
			handleComplete(orderId) {
				completeService(orderId).then(() => {
					this.$modal.msgSuccess("服务完成");
					this.loadData();
				});
			},
			navToDetail(orderId) {
				this.$tab.navigateTo(`/pages/order/detail?orderId=${orderId}`);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.worker-dashboard {
		padding: 20rpx;
		
		.stats-card {
			border-radius: 16rpx;
			overflow: hidden;
		}

		.nav-tabs {
			display: flex;
			height: 90rpx;
			line-height: 90rpx;
			text-align: center;
			border-radius: 10rpx;
			
			.cu-item {
				&.cur {
					border-bottom: 4rpx solid;
					font-weight: bold;
				}
			}
		}

		.order-card {
			.w-150 { width: 160rpx; flex-shrink: 0; }
			.border-bottom { border-bottom: 1rpx solid #eee; }
			.border-top { border-top: 1rpx solid #eee; }
		}
		
		.empty-box {
			padding-top: 100rpx;
		}
	}
</style>
