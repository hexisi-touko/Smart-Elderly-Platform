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
					<button v-if="item.orderStatus == 2" @click="openCompletePopup(item.orderId)" class="cu-btn bg-green round sm margin-left-sm">确认完成</button>
					<button @click="navToDetail(item.orderId)" class="cu-btn line-blue round sm margin-left-sm">查看详情</button>
				</view>
			</view>
		</scroll-view>

		<!-- 完成服务确认弹窗 -->
		<uni-popup ref="completePopup" type="bottom">
			<view class="complete-popup bg-white padding-xl radius-top">
				<view class="text-xl text-bold margin-bottom">服务完成确认</view>
				
				<view class="margin-bottom">
					<view class="text-df margin-bottom-xs">服务总结内容：</view>
					<textarea v-model="completeForm.serviceRecord" placeholder="请输入本次服务的总结说明..." class="bg-gray padding-sm radius w-100" style="height: 200rpx;"></textarea>
				</view>

				<view class="margin-bottom">
					<view class="text-df margin-bottom-xs">现场照片 (最多3张)：</view>
					<view class="grid col-3 grid-square">
						<view class="bg-img" v-for="(img, index) in fileList" :key="index" @tap="viewImage(index)">
							<image :src="img" mode="aspectFill"></image>
							<view class="cu-tag bg-red" @tap.stop="delImg(index)">
								<text class="cuIcon-close"></text>
							</view>
						</view>
						<view class="solids" @tap="chooseImage" v-if="fileList.length < 3">
							<text class="cuIcon-cameraadd"></text>
						</view>
					</view>
				</view>

				<view class="flex flex-direction">
					<button @click="handleComplete" class="cu-btn bg-green lg" :loading="submitting">确认提交</button>
					<button @tap="$refs.completePopup.close()" class="cu-btn line-gray lg margin-top">取消</button>
				</view>
			</view>
		</uni-popup>
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
				orderList: [],
				// 完成订单表单
				completeForm: {
					orderId: null,
					serviceRecord: '',
					servicePhotos: ''
				},
				fileList: [], // 存放临时图片路径或上传后的 URL
				submitting: false
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
			openCompletePopup(orderId) {
				this.completeForm.orderId = orderId;
				this.completeForm.serviceRecord = '';
				this.completeForm.servicePhotos = '';
				this.fileList = [];
				this.$refs.completePopup.open();
			},
			// 选择图片
			chooseImage() {
				uni.chooseImage({
					count: 3 - this.fileList.length,
					sizeType: ['compressed'],
					success: (res) => {
						// 循环上传图片
						res.tempFilePaths.forEach(path => {
							this.uploadImg(path);
						});
					}
				});
			},
			// 上传图片到后端（后端会代转 OSS）
			uploadImg(path) {
				const baseUrl = process.env.VUE_APP_BASE_API;
				uni.uploadFile({
					url: baseUrl + '/common/upload',
					filePath: path,
					name: 'file',
					header: {
						Authorization: "Bearer " + this.$store.state.user.token
					},
					success: (uploadRes) => {
						const data = JSON.parse(uploadRes.data);
						if (data.code === 200) {
							this.fileList.push(data.url);
						} else {
							this.$modal.msgError(data.msg || "上传失败");
						}
					}
				});
			},
			delImg(index) {
				this.fileList.splice(index, 1);
			},
			viewImage(index) {
				uni.previewImage({
					urls: this.fileList,
					current: index
				});
			},
			handleComplete() {
				if (!this.completeForm.serviceRecord) {
					return this.$modal.msgError("请输入服务总结内容");
				}
				this.submitting = true;
				// 将图片列表转为逗号隔开的字符串
				this.completeForm.servicePhotos = this.fileList.join(",");
				
				completeService(this.completeForm).then(() => {
					this.$modal.msgSuccess("服务已确认完成");
					this.$refs.completePopup.close();
					this.submitting = false;
					this.loadData();
				}).catch(() => {
					this.submitting = false;
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
